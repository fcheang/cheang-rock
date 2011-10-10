package com.suntek.scheduler.ui;

import java.awt.*;
import javax.swing.*;
import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.math.BigDecimal;
//import java.awt.event.*;

/** A simple utility class that lets you very simply print
 *  an arbitrary component. Just pass the component to the
 *  PrintUtilities.printComponent. The component you want to
 *  print doesn't need a print method and doesn't have to
 *  implement any interface or do anything special at all.
 *  <P>
 *  If you are going to be printing many times, it is marginally more
 *  efficient to first do the following:
 *  <PRE>
 *    PrintUtilities printHelper = new PrintUtilities(theComponent);
 *  </PRE>
 *  then later do printHelper.print(). But this is a very tiny
 *  difference, so in most cases just do the simpler
 *  PrintUtilities.printComponent(componentToBePrinted).
 *
 *  7/99 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 *  May be freely used or adapted.
 */

public class PrintUtils implements Printable {

    private static boolean debugOn = Boolean.getBoolean("debugOn");

    private Component componentToBePrinted;
    private int numPages;
    private double scaleX = 0.5;
    private double scaleY = 0.5;

    private static HashPrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();

    //private static PrintService currentService;

    public static void printComponent(Component c){
        (new PrintUtils(c)).print();
    }

    /*
    public static void printHTML(String htmlText) {
        if (htmlText == null) {
            return;
        }
        if (currentService == null) {
            debug("Warning: No print service available!");
            return;
        }
        Doc doc = new SimpleDoc(htmlText, DocFlavor.STRING.TEXT_HTML, null);
        DocPrintJob job = currentService.createPrintJob();
        try{
            job.print(doc, null);
        }catch(PrintException e){
            debug("Error: problem printing with exception "+e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.f, e);
        }
    }
*/

    /*
    public static void addPrintServices(JMenu menu) {
        DocFlavor favor = new DocFlavor("application/x-java-jvm-local-objectref", "java.awt.print.Pageable");
        PrintService[] services = PrintServiceLookup.lookupPrintServices(
            favor, null);
        ButtonGroup group = new ButtonGroup();
        debug("services.length = "+services.length);
        for (int i = 0; i < services.length; i++) {
            final PrintService service = services[i];
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(service.
                getName());
            menu.add(item);
            if (i == 0) {
                item.setSelected(true);
                currentService = service;
            }
            group.add(item);
            item.addActionListener(
                new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    currentService = service;
                    debug("set printService to "+service.getName());
                }
            });
        }
    }
*/

    public PrintUtils(Component c) {
        componentToBePrinted = c;
        //int h = componentToBePrinted.getHeight();
        //debug("component height = "+h);
        //numPages = h / 1200;
        //if ( (h % 1200) > 0 ){
        //    numPages += 1;
        //}
        //debug("numPages = "+numPages);
        numPages = 1;
    }

    public void print() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        Book book = new Book();
        book.append(this, printJob.defaultPage(), numPages);
        //printJob.setPrintable(this);
        printJob.setPageable(book);
        if (printJob.printDialog(attrs)) {
            try {
                printJob.print(attrs);
            }
            catch (PrinterException pe) {
                System.out.println("Error printing: " + pe);
            }
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        // pageIndex start from zero
        //if (pageIndex > 0) {
        if (pageIndex >= numPages){
            return (NO_SUCH_PAGE);
        }else{
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.scale(scaleX, scaleY);
            disableDoubleBuffering(componentToBePrinted);
            componentToBePrinted.paint(g2d);
            enableDoubleBuffering(componentToBePrinted);
            return (PAGE_EXISTS);
        }
    }

    /** The speed and quality of printing suffers dramatically if
     *  any of the containers have double buffering turned on.
     *  So this turns if off globally.
     *  @see enableDoubleBuffering
     */
    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    /** Re-enables double buffering globally. */

    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }

    private static void debug(String info){
        if (debugOn){
            System.out.println("[PrintUtils]: "+info);
        }
    }
}
