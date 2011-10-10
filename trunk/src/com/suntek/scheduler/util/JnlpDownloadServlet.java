package com.suntek.scheduler.util;

import javax.servlet.http.*;
import javax.servlet.*;

import com.suntek.scheduler.appsvcs.persistence.Constant;

import java.io.*;

public class JnlpDownloadServlet extends HttpServlet {

    //placedholders to be replaced during dynamic JNLP generation
    private final static String HOST = "$$host";

    //mime type to be set on response
    private final static String JNLP_MIME_TYPE = "application/x-java-jnlp-file; charset=utf-8";
    private final static String HTML_MIME_TYPE = "text/html; charset=utf-8";
        
    private static String indexHtml = 
    "<html> \n"+
    "<SCRIPT LANGUAGE=\"JavaScript\"> \n"+
    "var javaws160Installed=0; \n"+
    "var javaws170Installed=0; \n"+
    "isIE = \"false\"; \n"+
    "if (navigator.mimeTypes && navigator.mimeTypes.length) { \n"+
    "  x = navigator.mimeTypes['application/x-java-jnlp-file']; \n"+
    "  if (x) { \n"+
    "    javawsInstalled = 1; \n"+
    "    javaws142Installed=1; \n"+
    "    javaws150Installed=1; \n"+
    "  } \n"+
    "}else{ \n"+
    "  isIE = \"true\"; \n"+
    "} \n"+
    "</SCRIPT> \n"+
    "<SCRIPT LANGUAGE=\"VBScript\"> \n"+
    "on error resume next \n"+
    "If isIE = \"true\" Then \n"+
    "  If Not(IsObject(CreateObject(\"JavaWebStart.isInstalled.1.6.0.0\"))) Then \n"+
    "     javaws160Installed = 0 \n"+
    "  Else \n"+
    "     javaws160Installed = 1 \n"+
    "  End If \n"+
    "  If Not(IsObject(CreateObject(\"JavaWebStart.isInstalled.1.7.0.0\"))) Then \n"+
    "     javaws170Installed = 0 \n"+
    "  Else \n"+
    "     javaws170Installed = 1 \n"+
    "  End If \n"+
    "End If \n"+
    "</SCRIPT> \n"+
    "<head> \n"+
    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"> \n"+
    "<title>BHR System</title> \n"+
    "<style type=\"text/css\"> \n"+
    "<!-- \n"+
    ".style1 { \n"+
    "	font-size: 36px; \n"+
    "	font-weight: bold; \n"+
    "	color: #990000; \n"+
    "} \n"+
    "body { \n"+
    "	background-color: #FFFFFF; \n"+
    "} \n"+
    "--> \n"+
    "</style> \n"+
    "<script language=\"JavaScript\" type=\"text/JavaScript\"> \n"+
    "<!-- \n"+
    "function MM_swapImgRestore() { //v3.0 \n"+
    "  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc; \n"+
    "} \n"+
    "function MM_preloadImages() { //v3.0 \n"+
    "  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array(); \n"+
    "    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++) \n"+
    "    if (a[i].indexOf(\"#\")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}} \n"+
    "} \n"+
    "function MM_findObj(n, d) { //v4.01 \n"+
    "  var p,i,x;  if(!d) d=document; if((p=n.indexOf(\"?\"))>0&&parent.frames.length) { \n"+
    "    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);} \n"+
    "  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n]; \n"+
    "  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document); \n"+
    "  if(!x && d.getElementById) x=d.getElementById(n); return x; \n"+
    "} \n"+
    "function MM_swapImage() { //v3.0 \n"+
    "  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3) \n"+
    "   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];} \n"+
    "} \n"+
    "//    -->\n"+
    "</script>\n"+
    "</head>\n"+
    "<script language=\"JavaScript\">\n"+
    "if (javaws160Installed || javaws170Installed || (navigator.userAgent.indexOf(\"Gecko\") !=-1)) {\n"+
    "    document.write(\"<body onLoad=\\\"MM_preloadImages(\\'intakesystem_down.jpg\\',\\'scheduler_down.jpg\\')\\\"><div align=center class=style1><p>BHR Medical System Access Version "+Constant.version+"</p><p><a href=\\\"http://$$host:8090/scheduler/launch.jnlp\\\" onMouseOut=\\\"MM_swapImgRestore()\\\" onMouseOver=\\\"MM_swapImage(\\'Image2\\',\\'\\',\\'scheduler_down.jpg\\',1)\\\"><img src=\\\"scheduler.jpg\\\" alt=\\\"Scheudler\\\" name=\\\"Image2\\\" width=155 height=93 border=0></a></p><p><a href=\\\"https://$$host:8443/bhr/login.jsp\\\" onMouseOut=\\\"MM_swapImgRestore()\\\" onMouseOver=\\\"MM_swapImage(\\'Image1\\',\\'\\',\\'intakesystem_down.jpg\\',1)\\\"><img src=\\\"intakesystem.jpg\\\" alt=\\\"Intake System\\\" name=\\\"Image1\\\" width=155 height=93 border=0></a></p></div></body>\");\n"+
    "} else {\n"+
    "    document.write(\"Click \");\n"+
    "    document.write(\"<a href=http://java.sun.com/PluginBrowserCheck?pass=http://$$host:8090/scheduler/download.html&fail=http://java.sun.com/javase/downloads/index.jsp>here</a> \");\n"+
    "    document.write(\"to download and install JRE 6 and the application.\");\n"+
    "}\n"+
    "</SCRIPT>\n"+
    "</html>\n";

    	        
    private static String downloadHtml = 
		"<HTML>"+
		"<BODY>"+
		"<OBJECT codebase=\"http://javTa.sun.com/update/1.6.0/jinstall-6u18-windows-i586.cab#Version=6,0,0,0\""+ 
		"classid=\"clsid:5852F5ED-8BF4-11D4-A245-0080C6F74284\" height=0 width=0>"+
		"<PARAM name=\"app\" value=\"http://$$host:8090/scheduler/launch.jnlp\">"+
		"<PARAM name=\"back\" value=\"true\">"+
		"<!-- Alternate HTML for browsers which cannot instantiate the object -->"+
		"<A href=\"http://java.sun.com/javase/downloads/index.jsp\">"+
		"Download Java Web Start</A>"+
		"</OBJECT>"+
		"</BODY>"+
		"</HTML>";    	
    
    private static String launchJnlp = 
	    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
	    "<jnlp codebase=\"http://$$host:8090/scheduler/\">"+
	       "<information>"+
	           "<title>Appointment Scheduler "+Constant.version+"</title>"+
	           "<vendor>Sunteksystems</vendor>"+
	           "<description>www.sunteksystems.com version "+Constant.version+"</description>"+
	           "<icon href=\"images/about.png\"/>"+
	       "</information>"+
	       "<security>"+
	         "<all-permissions/>"+
	       "</security>"+
	       "<resources>"+
	         "<j2se version=\"1.6+\"/ initial-heap-size=\"256M\" max-heap-size=\"1024M\"/>"+
	         "<jar href=\"scheduler.jar\"/>"+
	         "<jar href=\"mysql-connector-java-5.1.6-bin.jar\"/>"+
	         "<jar href=\"jdbc2_0-stdext.jar\"/>"+
	         "<jar href=\"jta-spec1_0_1.jar\"/>"+
	         "<jar href=\"jcalendar.jar\"/>"+
	         "<jar href=\"servlet.jar\"/>"+
	         "<property name=\"dbDriver\" value=\"com.mysql.jdbc.Driver\"/>"+
	         "<property name=\"dbHost\" value=\"$$host\"/>"+
	         "<property name=\"dbUsed\" value=\"gold\"/>"+
	         "<property name=\"dbUser\" value=\"steve\"/>"+
	         "<property name=\"dbPassword\" value=\"st3v3\"/>"+
	         "<property name=\"debugOn\" value=\"false\"/>"+
	       "</resources>"+
	       "<application-desc main-class=\"com.suntek.scheduler.ui.MainApplication\"/>"+
	    "</jnlp>";

/*
    private static String indexHtml = 
        "<html> \n"+
        "<SCRIPT LANGUAGE=\"JavaScript\"> \n"+
        "var javawsInstalled = 0; \n"+  
        "var javaws142Installed=0; \n"+
        "var javaws150Installed=0; \n"+
        "isIE = \"false\"; \n"+
        "if (navigator.mimeTypes && navigator.mimeTypes.length) { \n"+
        "  x = navigator.mimeTypes['application/x-java-jnlp-file']; \n"+
        "  if (x) { \n"+
        "    javawsInstalled = 1; \n"+
        "    javaws142Installed=1; \n"+
        "    javaws150Installed=1; \n"+
        "  } \n"+
        "}else{ \n"+
        "  isIE = \"true\"; \n"+
        "} \n"+
        "</SCRIPT> \n"+
        "<SCRIPT LANGUAGE=\"VBScript\"> \n"+
        "on error resume next \n"+
        "If isIE = \"true\" Then \n"+
        "  If Not(IsObject(CreateObject(\"JavaWebStart.isInstalled\"))) Then \n"+
        "     javawsInstalled = 0 \n"+
        "  Else \n"+
        "     javawsInstalled = 1 \n"+
        "  End If \n"+
        "  If Not(IsObject(CreateObject(\"JavaWebStart.isInstalled.1.4.2.0\"))) Then \n"+
        "     javaws142Installed = 0 \n"+
        "  Else \n"+
        "     javaws142Installed = 1 \n"+
        "  End If \n"+
        "  If Not(IsObject(CreateObject(\"JavaWebStart.isInstalled.1.5.0.0\"))) Then \n"+
        "     javaws150Installed = 0 \n"+
        "  Else \n"+
        "     javaws150Installed = 1 \n"+
        "  End If \n"+
        "End If \n"+
        "</SCRIPT> \n"+
        "<head> \n"+
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"> \n"+
        "<title>BHR System</title> \n"+
        "<style type=\"text/css\"> \n"+
        "<!-- \n"+
        ".style1 { \n"+
        "	font-size: 36px; \n"+
        "	font-weight: bold; \n"+
        "	color: #990000; \n"+
        "} \n"+
        "body { \n"+
        "	background-color: #FFFFFF; \n"+
        "} \n"+
        "--> \n"+
        "</style> \n"+
        "<script language=\"JavaScript\" type=\"text/JavaScript\"> \n"+
        "<!-- \n"+
        "function MM_swapImgRestore() { //v3.0 \n"+
        "  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc; \n"+
        "} \n"+
        "function MM_preloadImages() { //v3.0 \n"+
        "  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array(); \n"+
        "    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++) \n"+
        "    if (a[i].indexOf(\"#\")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}} \n"+
        "} \n"+
        "function MM_findObj(n, d) { //v4.01 \n"+
        "  var p,i,x;  if(!d) d=document; if((p=n.indexOf(\"?\"))>0&&parent.frames.length) { \n"+
        "    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);} \n"+
        "  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n]; \n"+
        "  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document); \n"+
        "  if(!x && d.getElementById) x=d.getElementById(n); return x; \n"+
        "} \n"+
        "function MM_swapImage() { //v3.0 \n"+
        "  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3) \n"+
        "   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];} \n"+
        "} \n"+
        "//    -->\n"+
        "</script>\n"+
        "</head>\n"+
        "<script language=\"JavaScript\">\n"+
        "if (javawsInstalled || javaws142Installed || javaws150Installed || (navigator.userAgent.indexOf(\"Gecko\") !=-1)) {\n"+
        "    document.write(\"<body onLoad=\\\"MM_preloadImages(\\'intakesystem_down.jpg\\',\\'scheduler_down.jpg\\')\\\"><div align=center class=style1><p>BHR Medical System Access Version "+Constant.version+"</p><p><a href=\\\"http://$$host:8090/scheduler/launch.jnlp\\\" onMouseOut=\\\"MM_swapImgRestore()\\\" onMouseOver=\\\"MM_swapImage(\\'Image2\\',\\'\\',\\'scheduler_down.jpg\\',1)\\\"><img src=\\\"scheduler.jpg\\\" alt=\\\"Scheudler\\\" name=\\\"Image2\\\" width=155 height=93 border=0></a></p><p><a href=\\\"https://$$host:8443/bhr/login.jsp\\\" onMouseOut=\\\"MM_swapImgRestore()\\\" onMouseOver=\\\"MM_swapImage(\\'Image1\\',\\'\\',\\'intakesystem_down.jpg\\',1)\\\"><img src=\\\"intakesystem.jpg\\\" alt=\\\"Intake System\\\" name=\\\"Image1\\\" width=155 height=93 border=0></a></p></div></body>\");\n"+
        "} else {\n"+
        "    document.write(\"Click\");\n"+
        "    document.write(\"<a href=http://java.sun.com/PluginBrowserCheck?pass=http://$$host:8090/scheduler/download.html&fail=http://java.sun.com/j2se/1.5.0/download.html>here</a> \");\n"+
        "    document.write(\"to download and install JRE 5.0 and the application.\");\n"+
        "}\n"+
        "</SCRIPT>\n"+
        "</html>\n";

        	
        
        private static String downloadHtml = 
    		"<HTML>"+
    		"<BODY>"+
    		"<OBJECT codebase=\"http://java.sun.com/update/1.5.0/jinstall-1_5_0-windows-i586.cab\""+ 
    		"classid=\"clsid:5852F5ED-8BF4-11D4-A245-0080C6F74284\" height=0 width=0>"+
    		"<PARAM name=\"app\" value=\"http://$$host:8090/scheduler/launch.jnlp\">"+
    		"<PARAM name=\"back\" value=\"true\">"+
    		"<!-- Alternate HTML for browsers which cannot instantiate the object -->"+
    		"<A href=\"http://java.sun.com/j2se/1.5.0/download.html\">"+
    		"Download Java Web Start</A>"+
    		"</OBJECT>"+
    		"</BODY>"+
    		"</HTML>";    	
        
        private static String launchJnlp = 
    	    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
    	    "<jnlp codebase=\"http://$$host:8090/scheduler/\">"+
    	       "<information>"+
    	           "<title>Appointment Scheduler "+Constant.version+"</title>"+
    	           "<vendor>Sunteksystems</vendor>"+
    	           "<description>www.sunteksystems.com version "+Constant.version+"</description>"+
    	           "<icon href=\"images/about.png\"/>"+
    	       "</information>"+
    	       "<security>"+
    	         "<all-permissions/>"+
    	       "</security>"+
    	       "<resources>"+
    	         "<j2se version=\"1.5+\"/ initial-heap-size=\"256M\" max-heap-size=\"1024M\"/>"+
    	         "<jar href=\"scheduler.jar\"/>"+
    	         "<jar href=\"mysql-connector-java-5.1.6-bin.jar\"/>"+
    	         "<jar href=\"jdbc2_0-stdext.jar\"/>"+
    	         "<jar href=\"jta-spec1_0_1.jar\"/>"+
    	         "<jar href=\"jcalendar.jar\"/>"+
    	         "<jar href=\"servlet.jar\"/>"+
    	         "<property name=\"dbDriver\" value=\"com.mysql.jdbc.Driver\"/>"+
    	         "<property name=\"dbHost\" value=\"$$host\"/>"+
    	         "<property name=\"dbUsed\" value=\"gold\"/>"+
    	         "<property name=\"dbUser\" value=\"steve\"/>"+
    	         "<property name=\"dbPassword\" value=\"st3v3\"/>"+
    	         "<property name=\"debugOn\" value=\"false\"/>"+
    	       "</resources>"+
    	       "<application-desc main-class=\"com.suntek.scheduler.ui.MainApplication\"/>"+
    	    "</jnlp>";
*/    
    
    /**
    * Servlet initializer. Reads certain init parameters required for
    * the generation of JNLPs
    */
    public void init( ServletConfig config ) throws ServletException{
        super.init( config );
    }

    public  void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        doGet(req, res);
    }

    /**
     * Responds to each request by dynamically constructing a JNLP file
     * by replacing the $$ macros with the right values.
     *
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     */
    public  void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        try {
        	String path = req.getServletPath();
        	StringBuffer sb = null;
        	if (path.contains("index.html")){
        		sb = new StringBuffer(indexHtml);
        	}else if (path.contains("download.html")){
        		sb = new StringBuffer(downloadHtml);
        	}else if (path.contains("launch.jnlp")){
        		sb = new StringBuffer(launchJnlp);
        	}
        	String host = req.getServerName();
        	replaceHost(sb, host);
        	
            if( sb == null ){
                res.sendError( HttpServletResponse.SC_NOT_FOUND );
            } else {
            	if (path.contains("jnlp")){            		
            		res.setContentType(JNLP_MIME_TYPE);
            	}else{
            		res.setContentType(HTML_MIME_TYPE);
            	}

                ByteArrayOutputStream bytes = new ByteArrayOutputStream(1024);
                PrintWriter out = new PrintWriter(bytes, true);
                out.println(sb);
                res.setContentLength(bytes.size());
                bytes.writeTo(res.getOutputStream());
            }
        }
        catch(IOException e){
            res.sendError( HttpServletResponse.SC_NOT_FOUND );
        }
    }
        
    private static void replaceHost(StringBuffer sb, String host){
    	if (sb != null){
    		while (true){
    			int start = sb.indexOf(HOST);
    			if( start < 0 ){
    				return;
    			}
    			String val = host;
    			int length = HOST.length();
    			sb.replace( start, start+length, val );
    		}
    	}
    }
    
}
