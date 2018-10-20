package com.rustedbrain.study.course.view.cinema;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.*;
import com.rustedbrain.study.course.model.dto.TicketInfo;
import com.rustedbrain.study.course.service.util.TicketInfoHTML;
import com.rustedbrain.study.course.view.VaadinUI;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@UIScope
@SpringView(name = VaadinUI.TICKET_INFO_VIEW)
public class TicketsInfoViewImpl extends VerticalLayout implements TicketsInfoView {

	private static final long serialVersionUID = 54340880029187075L;

	private List<TicketsInfoView.TicketsInfoViewListener> viewListeners = new ArrayList<>();

	private Panel ticketsInfoPanel;

	public TicketsInfoViewImpl() {
		addComponent(getTicketsInfoPanel());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		viewListeners.forEach(listener -> listener.entered(event));
	}

	public Panel getTicketsInfoPanel() {
		if ( ticketsInfoPanel == null ) {
			ticketsInfoPanel = new Panel();
		}
		return ticketsInfoPanel;
	}

	@Override
	@Autowired
	public void addListener(TicketsInfoView.TicketsInfoViewListener listener) {
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
					priceLabel.setValue(priceLabel.getValue()
							+ "; Ticket reserved, please present it to the seller 30 minutes before the session.");
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

			// Set meta tags
			PdfDocumentInfo pdfMetaData = pdfDoc.getDocumentInfo();
			pdfMetaData.setAuthor("cinema-web-app Administrator");
			pdfMetaData.addCreationDate();
			pdfMetaData.getProducer();
			pdfMetaData.setCreator("cinema-web-app");
			pdfMetaData.setKeywords("ticket, tickets");
			pdfMetaData.setSubject("tickets");
			// Title is derived from html

			// pdf conversion
			ConverterProperties props = new ConverterProperties();

			HtmlConverter.convertToPdf(src, pdfDoc, props);
			pdfDoc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteArrayOutputStream;
	}
}
