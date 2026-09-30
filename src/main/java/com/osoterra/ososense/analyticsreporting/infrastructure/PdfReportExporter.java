package com.osoterra.ososense.analyticsreporting.infrastructure;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.osoterra.ososense.analyticsreporting.domain.model.PlotReport;
import com.osoterra.ososense.analyticsreporting.domain.model.ReportSection;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Renders a {@code PlotReport} and its sections to a PDF, for the report export
 * endpoint.
 */
@Component
public class PdfReportExporter {

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD);
    private static final Font SECTION_FONT = new Font(Font.HELVETICA, 13, Font.BOLD);

    public byte[] export(PlotReport report, List<ReportSection> sections) {
        Document document = new Document();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, output);
            document.open();
            document.add(new Paragraph("Plot Report", TITLE_FONT));
            document.add(new Paragraph("Plot #" + report.getPlotId() + " — Period: " + report.getPeriodStartDate()
                    + " to " + report.getPeriodEndDate()));
            document.add(Chunk.NEWLINE);
            for (ReportSection section : sections) {
                document.add(new Paragraph(section.getTitle(), SECTION_FONT));
                document.add(new Paragraph(section.getContent()));
                document.add(Chunk.NEWLINE);
            }
        } catch (DocumentException e) {
            throw new IllegalStateException("Failed to render the plot report PDF", e);
        } finally {
            document.close();
        }
        return output.toByteArray();
    }
}
