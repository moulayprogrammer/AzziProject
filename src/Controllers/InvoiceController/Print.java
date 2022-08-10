package Controllers.InvoiceController;

import BddPackage.*;
import Models.*;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import org.apache.commons.io.FileUtils;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class Print {

    private final ProductOperation productOperation = new ProductOperation();
    private final ClientOperation clientOperation = new ClientOperation();

    private final Invoice invoice;
    private final Client client;
    private final ArrayList<ComponentInvoice> componentInvoices;
    private double debt ;
    private double pay ;
    private boolean fastPrint ;
    private boolean debtPrint ;

    public Print(Invoice invoice, ArrayList<ComponentInvoice> componentInvoices, double pay, double debt, boolean fastPrint, boolean debtPrint) {
        this.invoice = invoice;
        this.client = clientOperation.get(invoice.getIdClient());
        this.componentInvoices = componentInvoices;
        this.pay = pay;
        this.debt = debt;
        this.fastPrint = fastPrint;
        this.debtPrint = debtPrint;
    }

    public void CreatePdf(){
        try {
            final StringBuilder HTML = new StringBuilder();

            HTML.append("<!DOCTYPE html>")
                    .append("<html  lang=\"ar\" >")
                    .append("<head>")
                    .append("<link rel=\"stylesheet\" type=\"text/css\" href=")
                    .append("src/resource/pdfStyle.css \">")
                    .append("</head>")
                    .append("<body >")
                    .append("<div id=\"block_container\">")
                    .append("<div class=\"bloc; fact\">")
                    .append("<div >")
                    .append("<img src=")
                    .append("src/Images/full_logo.png \">")
                    .append("</div>")
                    .append("<h5 style=\"text-align: right;\">")
                    .append("<span class=\"label\" >").append(this.client.getName()).append(" </span>")
                    .append("<span class=\"label\" style=\"margin-right: 90px\"> الزبون : </span>")
                    .append("<span class=\"label\" >").append(invoice.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))).append(" </span>")
                    .append("<span class=\"label\"> التاريخ : </span>")
                    .append("</h5>")
                    .append("<h3>")
                    .append("<span class=\"label\" >").append(" 2022/ ").append(invoice.getNumber()).append(" </span>")
                    .append("<span class=\"label\"> فاتورة رقم : </span>")
                    .append("</h3>")
                    .append("<div>")
                    .append("<table>")
                    .append("<tr >")
                    .append("<th>المرجع</th>")
                    .append("<th>المنتج</th>")
                    .append("<th>سعر الوحدة</th>")
                    .append("<th>الكمية</th>" )
                    .append("<th>المجموع</th>")
                    .append("</tr>");

            double totPrice = 0.0;
            int totQte = 0;

            for (ComponentInvoice componentInvoice : this.componentInvoices){
                Product product = productOperation.get(componentInvoice.getIdProduct());
                double price = componentInvoice.getPrice();
                int qte = componentInvoice.getQte();

                totPrice += (price * qte);
                totQte += qte;

                HTML.append("<tr>")
                        .append("<td>").append(product.getReference()).append("</td>")
                        .append("<td>").append(product.getName()).append("</td>")
                        .append("<td>").append(String.format(Locale.FRANCE, "%,.2f", price )).append("</td>")
                        .append("<td>").append(qte).append("</td>")
                        .append("<td>").append(String.format(Locale.FRANCE, "%,.2f", price* qte )).append("</td>")
                        .append("</tr>");
            }
            HTML.append("<tr>")
                    .append("<td colspan=\"3\">").append("المجموع").append("</td>")
                    .append("<td>").append(totQte).append("</td>")
                    .append("<td>").append(String.format(Locale.FRANCE, "%,.2f", totPrice )).append("</td>")
                    .append("</tr>")
                    .append("</table>");
            if (debtPrint) {
                HTML.append("<div style=\"margin-top: 10px;\">")
                        .append("<table>")
                        .append("<tr>")
                        .append("<td style=\"width: 150px;\">الدين</td>")
                        .append("<td>").append(String.format(Locale.FRANCE, "%,.2f", this.debt )).append("</td>")
                        .append(" </tr>")
                        .append("<tr>")
                        .append("<td style=\"width: 150px;\">المجموع الكلي</td>")
                        .append("<td>").append(String.format(Locale.FRANCE, "%,.2f", this.debt + totPrice )).append("</td>")
                        .append(" </tr>")
                        .append("<tr>")
                        .append("<td style=\"width: 150px;\">المدفوع</td>")
                        .append("<td>").append(String.format(Locale.FRANCE, "%,.2f", this.pay )).append("</td>")
                        .append(" </tr>")
                        .append("<tr>")
                        .append("<td style=\"width: 150px;\">المتبقي</td>")
                        .append("<td>").append(String.format(Locale.FRANCE, "%,.2f",  ( this.debt + totPrice) - this.pay )).append("</td>")
                        .append(" </tr>")
                        .append("</table>")
                        .append(" </div>");
            }

            HTML.append(" </div>" +
                    "<h6> إمضاء الممون </h6>" +
                    " </div>" );

            HTML.append("<div class=\"block; vl\"> </div>" );

            HTML.append("<div id=\"block_container\">")
                    .append("<div class=\"bloc; fact\">")
                    .append("<div >")
                    .append("<img src=")
                    .append("src/Images/full_logo.png \">")
                    .append("</div>")
                    .append("<h5 style=\"text-align: right;\">")
                    .append("<span class=\"label\" >").append(client.getName()).append(" </span>")
                    .append("<span class=\"label\" style=\"margin-right: 90px\"> الزبون : </span>")
                    .append("<span class=\"label\" >").append(invoice.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))).append(" </span>")
                    .append("<span class=\"label\"> التاريخ : </span>")
                    .append("</h5>")
                    .append("<h5 style=\"text-align: right; margin-top: -13px;\">")
                    .append("<span class=\"label\" >").append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))).append(" </span>")
                    .append("<span class=\"label\" \"> التوقيت : </span>")
                    .append("</h5>")
                    .append("<h3>")
                    .append("<span class=\"label\"> وصل تسليم </span>")
                    .append("</h3>")

                    .append("<div>")
                    .append("<table>")
                    .append("<tr >")
                    .append("<th>المرجع</th>")
                    .append("<th>المنتج</th>")
                    .append("<th>الكمية</th>")
                    .append("<th>التسليم</th>" )
                    .append("</tr>");

            for (ComponentInvoice componentInvoice : this.componentInvoices){
                Product product = productOperation.get(componentInvoice.getIdProduct());
                double price = componentInvoice.getPrice();
                int qte = componentInvoice.getQte();

                totPrice+= (price * qte);
                totQte += qte;

                HTML.append("<tr>")
                        .append("<td>").append(product.getReference()).append("</td>")
                        .append("<td>").append(product.getName()).append("</td>")
                        .append("<td>").append(qte).append("</td>")
                        .append("<td>").append("</td>")
                        .append("</tr>");
            }
            HTML.append("</table>");

            HTML.append("</table>" +
                            " </div>" +
                            "<h6> إمضاء الممون </h6>" +
                            "</div>" )

                    .append(" </body>")
                    .append("</html>");

            try {
                String pathDocument = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
                String mainDirectoryPath = pathDocument + File.separator + "Production";
                File mainFile =  new File(mainDirectoryPath);

                if (!mainFile.exists()) FileUtils.forceMkdir(mainFile);

                String invoiceDirectory = mainDirectoryPath + File.separator + "Invoices" ;
                File invoiceFile = new File(invoiceDirectory);
                if (!invoiceFile.exists()) FileUtils.forceMkdir(invoiceFile);

                String dayDirectory = invoiceDirectory + File.separator + "invoices_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
                File dayFile = new File(dayDirectory);
                if (!dayFile.exists()) FileUtils.forceMkdir(dayFile);


                if (dayFile.exists()) {

                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("_HH-mm-ss");

                    String path = dayDirectory + File.separator + "invoice" + LocalDateTime.now().format(myFormatObj) + ".pdf";
                    FileOutputStream file = new FileOutputStream(path);

                    ConverterProperties converterProperties = new ConverterProperties();

                    final String FONT = "src/resource/HSDream-Regular.otf";
                    FontProvider fontProvider = new DefaultFontProvider(true, true, true);
                    FontProgram fontProgram = FontProgramFactory.createFont(FONT);
                    fontProvider.addFont(fontProgram, "HSDream-Regular");
                    converterProperties.setFontProvider(fontProvider);


                    PdfDocument template = new PdfDocument(new PdfWriter(file));
                    template.setDefaultPageSize(PageSize.A5.rotate());


                    HtmlConverter.convertToPdf(HTML.toString(), template, converterProperties);

                    if (fastPrint) Desktop.getDesktop().print(new File(path));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
