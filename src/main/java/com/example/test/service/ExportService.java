package com.example.test.service;

import com.example.test.entity.ContactEntity;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class ExportService {

    private StreamResource getPDFStream(ByteArrayOutputStream baos) {
        StreamResource resource = new StreamResource("export.pdf", (InputStreamFactory) () -> new ByteArrayInputStream(baos.toByteArray()));
        return resource;
    }

    @SneakyThrows
    public StreamResource exportPdf(ContactEntity contact) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm - dd/MM/yyyy");
        Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
        Document doc = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, baos);
        doc.open();
        Paragraph paragraph = new Paragraph("Export time: " + now.format(formatter), smallFont);
        doc.add(paragraph);
        Paragraph h1 = new Paragraph("Details");
        h1.setAlignment(Element.ALIGN_CENTER);
        doc.add(h1);
        doc.add(Chunk.NEWLINE);
        Chunk nameLabel = new Chunk("Name: ");
        Chunk nameValue = new Chunk(contact.getName());
        Paragraph nameParagraph = new Paragraph(nameLabel);
        nameParagraph.add(nameValue);
        doc.add(nameParagraph);

        Chunk telLabel = new Chunk("Phone: ");
        Chunk telValue = new Chunk(contact.getPhone());
        Paragraph telParagraph = new Paragraph(telLabel);
        telParagraph.add(telValue);
        doc.add(telParagraph);

        Chunk emailLabel = new Chunk("Email: ");
        Chunk emailValue = new Chunk(contact.getEmail());
        Paragraph emailParagraph = new Paragraph(emailLabel);
        emailParagraph.add(emailValue);
        doc.add(emailParagraph);

        doc.close();
        return getPDFStream(baos);
    }
}
