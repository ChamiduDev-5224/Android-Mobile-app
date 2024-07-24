package com.example.busseatbookingapp;

import android.content.Context;
import android.util.Log;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {

    public static File generateReservationPdf(Context context, Reservation reservation, String reservationId) {
        // Define the file path for the PDF
        File pdfFile = new File(context.getExternalFilesDir(null), "Reservation_" + reservationId + ".pdf");

        // Create the PDF file
        try {
            // Initialize PDF writer with the output stream of the PDF file
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
            // Create a PDF document
            PdfDocument pdf = new PdfDocument(writer);
            // Initialize the document
            Document document = new Document(pdf);

            // Add content to the document
            document.add(new Paragraph("Reservation Confirmation"));
            document.add(new Paragraph("Reservation ID: " + reservationId));
            document.add(new Paragraph("Seat Number: " + reservation.getSeatNumber()));
            document.add(new Paragraph("Checking Place: " + reservation.getCheckingPlace()));
            document.add(new Paragraph("Reserving Date: " + reservation.getReservingDate()));
            document.add(new Paragraph("Bus ID: " + reservation.getBusId()));
            document.add(new Paragraph("Status: " + reservation.getStatus()));

            // Close the document
            document.close();

        } catch (IOException e) {
            // Log the exception and return null if there's an error
            Log.e("PdfGenerator", "Error generating PDF: ", e);
            return null;
        }

        // Return the generated PDF file
        return pdfFile;
    }
}
