package com.cj.dentalclinic.service

import com.cj.dentalclinic.dto.BillDetailDto
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.Style
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.VerticalAlignment
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class PDFGenerator {

  companion object {

    fun invoicePDFReport(billDetailDto: BillDetailDto): ByteArrayInputStream {

      val outputStream = ByteArrayOutputStream()

      val pdfWriter = PdfWriter(outputStream)
      val pdfDocument = PdfDocument(pdfWriter)
      val document = Document(pdfDocument)

      val centerText = Style().setTextAlignment(TextAlignment.CENTER)

      val centerTextHeader =
        Style().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.LIGHT_GRAY)

      pdfDocument.defaultPageSize = PageSize.A4

      val col = 280f
      val headerColWidth = floatArrayOf(col, col)
      val headerTable = Table(headerColWidth)

      headerTable.addCell(
        Cell().add(Paragraph("INVOICE : ${billDetailDto.billDetail.billNo}"))
          .setVerticalAlignment(VerticalAlignment.MIDDLE)
          .setTextAlignment(TextAlignment.CENTER)
          .setFontSize(20f)
          .setBorder(Border.NO_BORDER)
      )
      headerTable.addCell(
        Cell().add(Paragraph("${billDetailDto.billDetail.clinicName}\nDahiri,Pune\nMob:9006788879"))
          .setTextAlignment(TextAlignment.RIGHT)
          .setBorder(Border.NO_BORDER)
      )

      document.add(headerTable)

      val solidLine = SolidLine(10f)
      solidLine.color = ColorConstants.WHITE
      document.add(LineSeparator(solidLine))


      val patientInfoColWidth = floatArrayOf(80f, 300f, 100f, 80f)
      val patientInfoTable = Table(patientInfoColWidth)

      patientInfoTable.caption = Div().add(Paragraph("Patient Information")).setFontSize(14f)

      patientInfoTable.addCell(Cell().add(Paragraph("Name :")).setBorder(Border.NO_BORDER))
      patientInfoTable.addCell(Cell().add(Paragraph(billDetailDto.billDetail.patientFullName)).setBorder(Border.NO_BORDER))
      patientInfoTable.addCell(Cell().add(Paragraph("Age :")).setBorder(Border.NO_BORDER))
      patientInfoTable.addCell(Cell().add(Paragraph(billDetailDto.billDetail.patientAge.toString())).setBorder(Border.NO_BORDER))

      patientInfoTable.addCell(Cell().add(Paragraph("Mobile :")).setBorder(Border.NO_BORDER))
      patientInfoTable.addCell(Cell().add(Paragraph("8745212312")).setBorder(Border.NO_BORDER))
      patientInfoTable.addCell(Cell().add(Paragraph("Sex :")).setBorder(Border.NO_BORDER))
      patientInfoTable.addCell(Cell().add(Paragraph(billDetailDto.billDetail.patientSex)).setBorder(Border.NO_BORDER))

      patientInfoTable.addCell(Cell().add(Paragraph("Problem :")).setBorder(Border.NO_BORDER))
      patientInfoTable.addCell(
        Cell(
          0,
          3
        ).add(Paragraph(billDetailDto.billDetail.patientProblem))
//          Paragraph(""))
          .setBorder(
            Border.NO_BORDER
          )
      )

      document.add(patientInfoTable)

      document.add(LineSeparator(solidLine))

      val billDetailColWidth = floatArrayOf(40f, 400f, 120f)
      val billDetailTable = Table(billDetailColWidth)

      billDetailTable.caption = Div().add(Paragraph("Bill Details")).setFontSize(14f)

      billDetailTable.addCell(Cell().add(Paragraph("#")).addStyle(centerTextHeader))
      billDetailTable.addCell(Cell().add(Paragraph("Treatment")).addStyle(centerTextHeader))
      billDetailTable.addCell(Cell().add(Paragraph("Fee")).addStyle(centerTextHeader))

      for (billLine in billDetailDto.billLines) {
        billDetailTable.addCell(Cell().add(Paragraph("1")).addStyle(centerText))
        billDetailTable.addCell(Cell().add(Paragraph(billLine.treatment)))
        billDetailTable.addCell(Cell().add(Paragraph(billLine.fee.toString())).setTextAlignment(TextAlignment.RIGHT))
      }
      billDetailTable.addCell(Cell(0, 2).add(Paragraph("Total")))
      billDetailTable.addCell(Cell().add(Paragraph(billDetailDto.billDetail.billTotal.toString())).setTextAlignment(TextAlignment.RIGHT))

      document.add(billDetailTable)

      val solidLine2 = SolidLine(50f)
      solidLine2.color = ColorConstants.WHITE

      document.add(LineSeparator(solidLine2))


      document.add(
        Paragraph("${billDetailDto.billDetail.doctorFullName}\n ${billDetailDto.billDetail.doctorQualification}\n Authorised Signatory").setTextAlignment(TextAlignment.CENTER)
          .setFixedPosition(PageSize.A4.width - 200f, 40f, 200f)
      )

      document.close()

      return ByteArrayInputStream(outputStream.toByteArray())
    }
  }
}