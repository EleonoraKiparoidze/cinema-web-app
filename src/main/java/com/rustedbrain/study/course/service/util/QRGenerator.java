package com.rustedbrain.study.course.service.util;

import java.io.ByteArrayOutputStream;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.core.scheme.EMail;
import net.glxn.qrgen.core.scheme.MMS;
import net.glxn.qrgen.core.scheme.MeCard;
import net.glxn.qrgen.core.scheme.SMS;
import net.glxn.qrgen.core.scheme.VCard;
import net.glxn.qrgen.javase.QRCode;

public class QRGenerator {

	public static void main(String[] args) {
		// get QR stream from text using defaults
		ByteArrayOutputStream stream = QRCode.from("Hello World").stream();

		// override the image type to be JPG
		QRCode.from("Hello World").to(ImageType.JPG).file();
		QRCode.from("Hello World").to(ImageType.JPG).stream();

		// override image size to be 250x250
		QRCode.from("Hello World").withSize(250, 250).file();
		QRCode.from("Hello World").withSize(250, 250).stream();

		// override size and image type
		QRCode.from("Hello World").to(ImageType.GIF).withSize(250, 250).file();
		QRCode.from("Hello World").to(ImageType.GIF).withSize(250, 250).stream();

		// override default colors (black on white)
		// notice that the color format is "0x(alpha: 1 byte)(RGB: 3 bytes)"
		// so in the example below it's red for foreground and yellowish for background, both 100% alpha (FF).
		QRCode.from("Hello World").withColor(0xFFFF0000, 0xFFFFFFAA).file();

		// supply own outputstream
		QRCode.from("Hello World").to(ImageType.PNG).writeTo(stream);

		// supply own file name
		QRCode.from("Hello World").file("QRCode");

		// supply charset hint to ZXING
		QRCode.from("Hello World").withCharset("UTF-8");

		// supply error correction level hint to ZXING
		QRCode.from("Hello World").withErrorCorrection(ErrorCorrectionLevel.L);

		// supply any hint to ZXING
		QRCode.from("Hello World").withHint(EncodeHintType.CHARACTER_SET, "UTF-8");

		// encode contact data as vcard using defaults
		VCard johnDoe = new VCard("John Doe").setEmail("john.doe@example.org")
				.setAddress("John Doe Street 1, 5678 Doestown").setTitle("Mister").setCompany("John Doe Inc.")
				.setPhoneNumber("1234").setWebsite("www.example.org");
		QRCode.from(johnDoe).file();

		// encode email data
		EMail email = new EMail("John.Doe@example.org");
		QRCode.from(email).file();

		// encode mms data
		MMS mms = new MMS();
		mms.setSubject("Hello World");
		QRCode.from(mms).file();

		// encode sms data
		SMS sms = new SMS();
		sms.setSubject("Hello World");
		QRCode.from(sms).file();

		// encode MeCard data
		MeCard johnDoeMeCard = new MeCard("Alexey Muhin");
		johnDoeMeCard.setEmail("alexeymuhinout@gmail.com");
		QRCode.from(johnDoeMeCard).file();

		// if using special characters don't forget to supply the encoding
		VCard johnSpecial = new VCard("Jöhn Dɵe").setAddress("ëåäöƞ Sträät 1, 1234 Döestüwn");
		QRCode.from(johnSpecial).withCharset("UTF-8").file();
	}
}
