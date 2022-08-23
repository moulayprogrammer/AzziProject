package Controllers.InvoiceController;

import BddPackage.*;
import Models.*;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

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

    public void CreatePdfFacture(){
        try {
            final StringBuilder HTMLFacture = new StringBuilder();

            HTMLFacture.append("<!DOCTYPE html>")
                    .append("<html  lang=\"ar\" >")
                    .append("<head>")
                    .append("<link rel=\"stylesheet\" type=\"text/css\" href=")
                    .append("src/resource/new_pdfStyle.css \">")
                    .append("</head>")
                    .append("<body >")

                    // header div
                    .append("<div id=\"block_container\" style=\"margin-left: 15px; margin-top: 20px;\" >")
                    .append("<div class=\"bloc\" style=\"margin-right: 10px;\" >")
                    .append("<h1 id=\"title\">")
                    .append("وصــــــل تســليــــــــم")
                    .append("</h1>")
                    .append("<h6 class=\"sub_title\" style=\"margin-top: -15px;\" >")
                    .append("العنوان : طريق الوزن الثقيل تبركات - تمنراست")
                    .append("</h6>")
                    .append("<div class=\"sub_title\" style=\"margin-top: -45px;\" >")

                    .append("<h6 class=\"label\">usinesud@gmail.com</h6>")
                    .append("<h6 class=\"label\" style=\"margin-right: 5px;\">  البريد الالكتروني : </h6>")
                    .append("<h6 class=\"label\">0660764614</h6>")
                    .append("<h6 class=\"label\"> رقم الهاتف : </h6>")

                    .append("</div>")
                    .append("</div>")
                    .append("<div class=\"bloc\" style=\"margin-top: -10px;\" >")
                    .append("<img src=")
                    .append("src/Images/logoN.jpeg \">")
                    .append("</div>")
                    .append("</div>")

                    // end header div

                    // info div
                    .append("<div id=\"info_div\" >")
                    .append("<div id=\"block_container\" >")

                    .append("<div class=\"bloc bloc_info\" >")

                    .append("<div style=\"margin-top: -20px;\">")
                    .append("<h6 class=\"label\">")
                    .append(client.getName())
                    .append("</h6>" )
                    .append("<h6 class=\"label_info\">  الزبون : </h6>")
                    .append("</div>")

                    .append("<div class=\"info\">")
                    .append("<h6 class=\"label\">")
                    .append(client.getReference())
                    .append("</h6>" )
                    .append("<h6 class=\"label_info\">  كود الزبون : </h6>")
                    .append("</div>")

                    .append("</div>")

                    .append("<div class=\"bloc bloc_info\"  >")

                    .append("<div style=\"margin-top: -20px;\">")
                    .append("<h6 class=\"label\">")
                    .append("2022 / ")
                    .append(invoice.getNumber())
                    .append("</h6>" )
                    .append("<h6 class=\"label_info\">  رقم الوصل : </h6>" )
                    .append("</div>" )

                    .append("<div class=\"info\">" )
                    .append("<h6 class=\"label\">")
                    .append(invoice.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")))
                    .append("</h6>" )
                    .append("<h6 class=\"label_info\">  التاريخ : </h6>" )
                    .append("</div>" )

                    .append("</div>" )

                    .append("</div>" )
                    .append("</div>")

                    // end info div

                    // tables div

                    .append("<div style=\"margin-top: 10px;\">" )
                    .append("<table class=\"table_content\">" )
                    .append("<tr >" )
                    .append(" <th>المرجع</th>" )
                    .append("<th>المنتج</th>" )
                    .append(" <th>سعر الوحدة</th>" )
                    .append("<th>الكمية</th>" )
                    .append("<th>المجموع</th>" )
                    .append("</tr>" );

            double totPrice = 0.0;

            for (ComponentInvoice componentInvoice : this.componentInvoices){
                Product product = productOperation.get(componentInvoice.getIdProduct());
                double price = componentInvoice.getPrice();
                int qte = componentInvoice.getQte();

                totPrice += (price * qte);

                HTMLFacture.append("<tr>")
                        .append("<td class=\"td_content\">").append(product.getReference()).append("</td>")
                        .append("<td class=\"td_content\">").append(product.getName()).append("</td>")
                        .append("<td class=\"td_content\">").append(String.format(Locale.FRANCE, "%,.2f", price )).append("</td>")
                        .append("<td class=\"td_content\">").append(qte).append("</td>")
                        .append("<td class=\"td_content\">").append(String.format(Locale.FRANCE, "%,.2f", price* qte )).append("</td>")
                        .append("</tr>");
            }

                    HTMLFacture.append("<table class=\"table_sum\">" )
                            .append("<tr>" )
                            .append("<td class=\"td_sum\">المجموع</td>" )
                            .append("<td class=\"td_sum\">").append(String.format(Locale.FRANCE, "%,.2f", totPrice )).append("</td>" )
                            .append("</tr>" );
            if (debtPrint) {

                HTMLFacture.append("<tr>" )
                        .append("<td class=\"td_sum\"> الدين </td>" )
                        .append("<td class=\"td_sum\">").append(String.format(Locale.FRANCE, "%,.2f", debt )).append("</td>" )
                        .append("</tr>" )

                        .append("<tr>" )
                        .append("<td class=\"td_sum\"> المجموع الكلي </td>" )
                        .append("<td class=\"td_sum\">").append(String.format(Locale.FRANCE, "%,.2f", totPrice + debt )).append("</td>" )
                        .append("</tr>" )

                        .append("</table>")

                        .append("<table class=\"table_sum\">" )

                        .append("<tr>" )
                        .append("<td class=\"td_sum\" style=\"background-color: lightgray;\">المدفوع</td>" )
                        .append("<td class=\"td_sum\">").append(String.format(Locale.FRANCE, "%,.2f", pay )).append("</td>" )
                        .append("</tr>" )

                        .append("<tr>" )
                        .append("<td class=\"td_sum\" style=\"background-color: lightgray;\">المتبقي </td>" )
                        .append("<td class=\"td_sum\">").append(String.format(Locale.FRANCE, "%,.2f",  (totPrice + debt) - pay )).append("</td>" )
                        .append("</tr>" );
            }

                            HTMLFacture.append("</table>" )
                                    .append("</div>")
                    // end tables content div
                                    .append("<h6 style=\"margin-top: 10px ; text-align: center;\"> إمضاء الممون </h6>")
                                    .append("</body>" )
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

                    String path = dayDirectory + File.separator + "invoice_" + LocalDateTime.now().format(myFormatObj) + ".pdf";
                    FileOutputStream file = new FileOutputStream(path);

                    ConverterProperties converterProperties = new ConverterProperties();

                    final String FONT = "src/resource/HSDream-Regular.otf";
                    FontProvider fontProvider = new DefaultFontProvider(true, true, true);
                    FontProgram fontProgram = FontProgramFactory.createFont(FONT);
                    fontProvider.addFont(fontProgram, "HSDream-Regular");
                    converterProperties.setFontProvider(fontProvider);

                    PdfDocument pdf = new PdfDocument(new PdfWriter(file));

                    HtmlConverter.convertToPdf(HTMLFacture.toString(), pdf, converterProperties);

                    pdf.close();

                    if (fastPrint) Desktop.getDesktop().print(new File(path));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void CreatePdfBon(){
        try {
            final StringBuilder HTMLBonReception = new StringBuilder();

            HTMLBonReception.append("<!DOCTYPE html>")
                    .append("<html  lang=\"ar\" >")
                    .append("<head>")
                    .append("<link rel=\"stylesheet\" type=\"text/css\" href=")
                    .append("src/resource/new_pdfStyle.css \">")
                    .append("</head>")
                    .append("<body >")

                    // header div
                    .append("<div id=\"block_container\" style=\"margin-left: 15px; margin-top: 20px;\" >")
                    .append("<div class=\"bloc\" style=\"margin-right: 10px;\" >")
                    .append("<h1 id=\"title\">")
                    .append("وصــــــل المخـــــــــزن")
                    .append("</h1>")
                    .append("<h6 class=\"sub_title\" style=\"margin-top: -15px;\" >")
                    .append("العنوان : طريق الوزن الثقيل تبركات - تمنراست")
                    .append("</h6>")
                    .append("<div class=\"sub_title\" style=\"margin-top: -45px;\" >")

                    .append("<h6 class=\"label\">usinesud@gmail.com</h6>")
                    .append("<h6 class=\"label\" style=\"margin-right: 5px;\">  البريد الالكتروني : </h6>")
                    .append("<h6 class=\"label\">0660764614</h6>")
                    .append("<h6 class=\"label\"> رقم الهاتف : </h6>")

                    .append("</div>")
                    .append("</div>")
                    .append("<div class=\"bloc\" style=\"margin-top: -10px;\" >")
                    .append("<img src=")
                    .append("src/Images/logoN.jpeg \">")
                    .append("</div>")
                    .append("</div>")

                    /*.append("<div id=\"block_container\" style=\"margin-left: 15px;\" >")
                    .append("<div class=\"bloc\" style=\"margin-right: 10px; margin-top: 10px;\" >")
                    .append("<h1 id=\"title\">")
                    .append("وصــــــل المخـــــــــزن")
                    .append("</h1>")
                    .append("<h6 class=\"sub_title\" style=\"margin-top: 0px;\" >")
                    .append("العنوان : طريق الوزن الثقيل تبركات - تمنراست")
                    .append("</h6>")
                    .append("<div class=\"sub_title\" style=\"margin-top: -30px;\" >")

                    .append("<h6 class=\"label\">usinesud@gmail.com</h6>")
                    .append("<h6 class=\"label\" style=\"margin-right: 5px;\">  البريد الالكتروني : </h6>")
                    .append("<h6 class=\"label\">0660764614</h6>")
                    .append("<h6 class=\"label\">  رقم الهاتف : </h6>")

                    .append("</div>")
                    .append("</div>")
                    .append("<div class=\"bloc\" style=\"margin-top: 5px;\" >")
                    .append("<img src=")
                    .append("src/Images/logoN.jpeg \">")
                    .append("</div>")
                    .append("</div>")
*/
                    // end header div

                    // info div
                    .append("<div id=\"info_div\" >")
                    .append("<div id=\"block_container\" >")

                    .append("<div class=\"bloc bloc_info\" >")

                    .append("<div style=\"margin-top: -20px;\">")
                    .append("<h6 class=\"label\">")
                    .append(client.getName())
                    .append("</h6>" )
                    .append("<h6 class=\"label_info\">  الزبون : </h6>")
                    .append("</div>")

                    .append("<div class=\"info\">")
                    .append("<h6 class=\"label\">")
                    .append(client.getReference())
                    .append("</h6>" )
                    .append("<h6 class=\"label_info\">  كود الزبون : </h6>")
                    .append("</div>")

                    .append("</div>")

                    .append("<div class=\"bloc bloc_info\"  >")

                    .append("<div style=\"margin-top: -20px;\">")
                    .append("<h6 class=\"label\">")
                    .append("2022 / ")
                    .append(invoice.getNumber())
                    .append("</h6>" )
                    .append("<h6 class=\"label_info\">  رقم الوصل : </h6>" )
                    .append("</div>" )

                    .append("<div class=\"info\">" )
                    .append("<h6 class=\"label\">")
                    .append(invoice.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")))
                    .append("</h6>" )
                    .append("<h6 class=\"label_info\">  التاريخ : </h6>" )
                    .append("</div>" )

                    .append("</div>" )

                    .append("</div>" )
                    .append("</div>")

                    // end info div

                    // tables div

                    .append("<div style=\"margin-top: 10px;\">" )
                    .append("<table class=\"table_content\">" )
                    .append("<tr >" )
                    .append("<th>المرجع</th>" )
                    .append("<th>المنتج</th>" )
                    .append("<th>الكمية</th>" )
                    .append("<th>التسليم</th>" )
                    .append("</tr>" );

            for (ComponentInvoice componentInvoice : this.componentInvoices){
                Product product = productOperation.get(componentInvoice.getIdProduct());

                HTMLBonReception.append("<tr>")
                        .append("<td class=\"td_content\">").append(product.getReference()).append("</td>")
                        .append("<td class=\"td_content\">").append(product.getName()).append("</td>")
                        .append("<td class=\"td_content\">").append(componentInvoice.getQte()).append("</td>")
                        .append("<td class=\"td_content\">").append("</td>")
                        .append("</tr>");
            }

            HTMLBonReception.append("</table>" )
                    .append("</div>")
                    // end tables content div
                    .append("<h6 style=\"margin-top: 10px ; text-align: center;\"> إمضاء الممون </h6>")
                    .append("</body>" )
                    .append("</html>");

            try {
                String pathDocument = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
                String mainDirectoryPath = pathDocument + File.separator + "Production";
                File mainFile =  new File(mainDirectoryPath);

                if (!mainFile.exists()) FileUtils.forceMkdir(mainFile);

                String invoiceDirectory = mainDirectoryPath + File.separator + "Bons" ;
                File invoiceFile = new File(invoiceDirectory);
                if (!invoiceFile.exists()) FileUtils.forceMkdir(invoiceFile);

                String dayDirectory = invoiceDirectory + File.separator + "bons_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
                File dayFile = new File(dayDirectory);
                if (!dayFile.exists()) FileUtils.forceMkdir(dayFile);


                if (dayFile.exists()) {

                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("_HH-mm-ss");

                    String path = dayDirectory + File.separator + "bon_" + LocalDateTime.now().format(myFormatObj) + ".pdf";
                    FileOutputStream file = new FileOutputStream(path);

                    ConverterProperties converterProperties = new ConverterProperties();

                    final String FONT = "src/resource/HSDream-Regular.otf";
                    FontProvider fontProvider = new DefaultFontProvider(true, true, true);
                    FontProgram fontProgram = FontProgramFactory.createFont(FONT);
                    fontProvider.addFont(fontProgram, "HSDream-Regular");
                    converterProperties.setFontProvider(fontProvider);

                    PdfDocument pdf = new PdfDocument(new PdfWriter(file));

                    HtmlConverter.convertToPdf(HTMLBonReception.toString(), pdf, converterProperties);

                    pdf.close();

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
