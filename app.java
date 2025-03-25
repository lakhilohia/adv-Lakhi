import java.io.*;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.UUID;

public class App extends HttpServlet {
    
    private static final String CSS_STYLE = "<link rel='stylesheet' href='style.css'>";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
            // Validate and sanitize input
            String serviceType = sanitizeInput(request.getParameter("service-select"));
            
            if(serviceType == null || serviceType.isEmpty()) {
                sendErrorResponse(out, "Please select a valid service");
                return;
            }
            
            // Generate booking reference
            String bookingRef = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            // Build confirmation page
            out.println(buildHeader("Booking Confirmation"));
            out.println(buildConfirmationBody(serviceType, bookingRef));
            out.println(buildAboutSection());
            out.println(buildFooter());
            
        } catch (Exception e) {
            log("Servlet error: " + e.getMessage());
            sendErrorResponse(out, "An error occurred processing your request");
        } finally {
            out.close();
        }
    }

    private String sanitizeInput(String input) {
        if(input == null) return null;
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    private void sendErrorResponse(PrintWriter out, String message) {
        out.println(buildHeader("Error"));
        out.println("<div class='error-container'>");
        out.println("<h2>Error Processing Request</h2>");
        out.println("<p>" + message + "</p>");
        out.println("<a href='index.html' class='btn'>Return Home</a>");
        out.println("</div>");
        out.println(buildFooter());
    }

    private String buildHeader(String title) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
               "<title>" + title + "</title>" +
               CSS_STYLE +
               "<script>" +
               "function toggleAboutDetails() {" +
               "  const elements = document.querySelectorAll('#about-details, #social-dropdown');" +
               "  elements.forEach(el => el.classList.toggle('hidden'));" +
               "}" +
               "</script>" +
               "</head>" +
               "<body>";
    }

    private String buildConfirmationBody(String serviceType, String bookingRef) {
        return "<section class='confirmation-container'>" +
               "<h2>🎉 Booking Confirmed!</h2>" +
               "<div class='confirmation-details'>" +
               "<p><strong>Service Type:</strong> " + serviceType + "</p>" +
               "<p><strong>Reference Number:</strong> " + bookingRef + "</p>" +
               "<p><strong>Date:</strong> " + new Date() + "</p>" +
               "</div>" +
               "<div class='next-steps'>" +
               "<h3>Next Steps:</h3>" +
               "<ol>" +
               "<li>Check your email for confirmation</li>" +
               "<li>Complete payment within 24 hours</li>" +
               "<li>Our team will contact you for documentation</li>" +
               "</ol>" +
               "</div>" +
               "<a href='index.html' class='btn'>Return to Home</a>" +
               "</section>";
    }

    private String buildAboutSection() {
        return "<section id='about' class='content-section'>" +
               "<h2 onclick='toggleAboutDetails()'>About Us ▼</h2>" +
               "<div id='about-details' class='hidden'>" +
               "<img src='founder.jpg' alt='Adv. Lakhi Lohia' class='profile-img'>" +
               "<div class='bio'>" +
               "<p>Adv. Lakhi Lohia, M.Com, LLB with 12+ years experience in:</p>" +
               "<ul>" +
               "<li>Taxation & GST Compliance</li>" +
               "<li>Corporate Legal Advisory</li>" +
               "<li>Financial Auditing</li>" +
               "<li>Debt Recovery Solutions</li>" +
               "</ul>" +
               "<div id='social-dropdown' class='hidden'>" +
               "<h3>Connect With Us:</h3>" +
               "<a href='https://facebook.com' class='social-link'>Facebook</a>" +
               "<a href='https://instagram.com' class='social-link'>Instagram</a>" +
               "<a href='https://linkedin.com' class='social-link'>LinkedIn</a>" +
               "</div>" +
               "</div>" +
               "</div>" +
               "</section>";
    }

    private String buildFooter() {
        return "<footer>" +
               "<div class='footer-content'>" +
               "<p>Need immediate assistance? Call: <a href='tel:7002926537'>7002926537</a></p>" +
               "<p>© " + new Date().getYear() + 1900 + " Lohia Consultancy Services</p>" +
               "</div>" +
               "</footer>" +
               "</body>" +
               "</html>";
    }
}