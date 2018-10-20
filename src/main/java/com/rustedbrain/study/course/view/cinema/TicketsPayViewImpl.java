package com.rustedbrain.study.course.view.cinema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.liqpay.LiqPay;
import com.rustedbrain.study.course.model.dto.TicketInfo;
import com.rustedbrain.study.course.service.util.TicketInfoHTML;
import com.rustedbrain.study.course.view.VaadinUI;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@UIScope
@SpringView(name = VaadinUI.TICKET_PAY_VIEW)
public class TicketsPayViewImpl extends VerticalLayout implements TicketsPayView {

	private final class CardBlurListener implements BlurListener {
		private final TextField cardNumber;
		private final String cardPattern;
		private final String errorMsg;

		private CardBlurListener(TextField cardNumber, String cardPattern, String errorMsg) {
			this.cardNumber = cardNumber;
			this.cardPattern = cardPattern;
			this.errorMsg = errorMsg;
		}

		@Override
		public void blur(BlurEvent event) {
			if ( !this.cardNumber.getValue().matches(this.cardPattern) ) {
				showError(errorMsg);
				this.cardNumber.setVisible(true);
			} else {
				this.cardNumber.setVisible(false);
			}
		}
	}

	private static final long serialVersionUID = -6036848728647707151L;
	private static final String PUBLIC_KEY = "public_key";
	private static final String PRIVATE_KEY = "private_key";
	private static final Logger logger = Logger.getLogger(TicketsPayViewImpl.class.getName());
	private List<TicketsPayView.TicketsPayViewListener> viewListeners = new ArrayList<>();
	private Panel ticketsInfoPanel;
	private String order_id;

	public TicketsPayViewImpl() {
		addComponent(getTicketsInfoPanel());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		order_id = event.getParameters();
		viewListeners.forEach(listener -> {
			try {
				listener.entered(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Panel getTicketsInfoPanel() {
		if ( ticketsInfoPanel == null ) {
			ticketsInfoPanel = new Panel();
		}
		return ticketsInfoPanel;
	}

	@Override
	@Autowired
	public void addListener(TicketsPayView.TicketsPayViewListener listener) {
		listener.setView(this);
		this.viewListeners.add(listener);
	}

	@Override
	public void showTicketsInfo(List<TicketInfo> ticketInfos) {
		VerticalLayout ticketsVerticalLayout = new VerticalLayout();
		if ( !ticketInfos.isEmpty() ) {
			for (TicketInfo ticketInfo : ticketInfos) {
				Label movieNameLabel = new Label(ticketInfo.getMovie());
				Label dateTimeLabel = new Label("Date: " + ticketInfo.getDate() + "; Time: " + ticketInfo.getDate());
				Label cinemaHallLabel = new Label("Hall: " + ticketInfo.getHall());
				Label rowSeatLabel = new Label("Row: " + ticketInfo.getRow() + "; Seat: " + ticketInfo.getSeat());
				Label priceLabel = new Label("Price: " + ticketInfo.getPrice());
				if ( ticketInfo.isReserved() ) {
					priceLabel.setValue(priceLabel.getValue() + "; Ticket purchased");
				}
				ticketsVerticalLayout.addComponent(new Panel(
						new VerticalLayout(movieNameLabel, dateTimeLabel, cinemaHallLabel, rowSeatLabel, priceLabel)));
			}
			Button buttonDownload = new Button("Download");
			StreamResource myResource = createResource(ticketInfos);
			FileDownloader fileDownloader = new FileDownloader(myResource);
			fileDownloader.extend(buttonDownload);

			ticketsVerticalLayout.addComponent(buttonDownload);
		} else {
			Label errorLabel = new Label("No tickets selected.");
			ticketsVerticalLayout.addComponent(errorLabel);
		}
		getTicketsInfoPanel().setContent(ticketsVerticalLayout);
	}

	@Override
	public void showWarning(String message) {
		Notification.show(message, Notification.Type.WARNING_MESSAGE);
	}

	@Override
	public void showError(String message) {
		Notification.show(message, Notification.Type.ERROR_MESSAGE);
	}

	@Override
	public void reload() {
		Page.getCurrent().reload();
	}

	private StreamResource createResource(List<TicketInfo> ticketInfos) {

		return new StreamResource((StreamResource.StreamSource) () -> {

			StringWriter stringWriter = new StringWriter();

			try {
				TicketInfoHTML.getInstance().precess(ticketInfos, stringWriter);

				return new ByteArrayInputStream(createPdfStream(stringWriter.toString()).toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}, "tickets.pdf");
	}

	public ByteArrayOutputStream createPdfStream(String src) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			WriterProperties writerProperties = new WriterProperties();
			// Add metadata
			writerProperties.addXmpMetadata();
			PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream, writerProperties);

			PdfDocument pdfDoc = new PdfDocument(pdfWriter);
			pdfDoc.getCatalog().setLang(new PdfString("en-US"));
			// Set the document to be tagged
			pdfDoc.setTagged();
			pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));

			PdfDocumentInfo pdfMetaData = pdfDoc.getDocumentInfo();
			pdfMetaData.setAuthor("cinema-web-app Administrator");
			pdfMetaData.addCreationDate();
			pdfMetaData.getProducer();
			pdfMetaData.setCreator("cinema-web-app");
			pdfMetaData.setKeywords("ticket, tickets");
			pdfMetaData.setSubject("tickets");
			ConverterProperties props = new ConverterProperties();

			HtmlConverter.convertToPdf(src, pdfDoc, props);
			pdfDoc.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return byteArrayOutputStream;
	}

	@Override
	public void showPayForm(List<TicketInfo> ticketInfos) {
		Panel payPanel = new Panel();
		VerticalLayout cardInfo = new VerticalLayout();

		TextField cardNumber = new TextField("Card Number");
		cardNumber.setWidth("200px");
		cardNumber.setPlaceholder("Card number");
		String cardPattern = "[0-9]{16}";
		cardNumber.addBlurListener(new CardBlurListener(cardNumber, cardPattern, "Card number must have 16 digits!"));

		HorizontalLayout validatyLayout = new HorizontalLayout();
		validatyLayout.setWidth("120px");
		validatyLayout.setCaption("Validity period");

		String validatyPattern = "[0-9]{2}";
		TextField month = new TextField();
		month.setPlaceholder("MM");
		month.addBlurListener(new CardBlurListener(month, validatyPattern, "Card month must have 2 digits!"));

		TextField year = new TextField();
		year.setPlaceholder("YY");
		year.addBlurListener(new CardBlurListener(year, validatyPattern, "Card year must have 2 digits!"));
		validatyLayout.addComponentsAndExpand(month, year);

		String cvvPattern = "[0-9]{3}";
		TextField cvv = new TextField("CVV");
		cvv.setPlaceholder("CVV");
		cvv.addBlurListener(new CardBlurListener(cvv, cvvPattern, "Card cvv must have 3 digits!"));
		cvv.setWidth("70px");

		double sum = ticketInfos.stream().mapToDouble(ticketInfo -> ticketInfo.getPrice()).sum();
		Map<String, String> params = new HashMap<>();
		params.put("version", "3");
		params.put("amount", String.valueOf(sum));
		params.put("currency", "UAN");
		params.put("sandbox", "1");
		params.put("description", "Tickets pay");
		params.put("order_id", order_id);
		params.put("card", cardNumber.getValue());
		params.put("card_exp_month", month.getValue());
		params.put("card_exp_year", year.getValue());
		params.put("card_cvv", cvv.getValue());
		LiqPay liqpay = new LiqPay(PUBLIC_KEY, PRIVATE_KEY);
		String html = liqpay.cnb_form(params);

		cardInfo.addComponent(new VerticalLayout(cardNumber, new HorizontalLayout(validatyLayout, cvv),
				new Label(html, ContentMode.HTML)));

		payPanel.setContent(cardInfo);
		getTicketsInfoPanel().setContent(payPanel);
//test
		Map<String, Object> res;
		try {
			res = liqpay.api("payment/auth", params);
			if ( "success".equals(res.get("status")) ) {
				showTicketsInfo(ticketInfos);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
}
