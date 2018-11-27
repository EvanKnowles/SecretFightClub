package za.co.knonchalant.images;

import za.co.knonchalant.liketosee.dao.ImagesDAO;
import za.co.knonchalant.liketosee.dao.MovieDAO;
import za.co.knonchalant.liketosee.domain.UpcomingMovie;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by evan on 2016/02/22.
 */
@WebServlet(description = "Image Servlet", urlPatterns = {"/images"})
public class DownloadServlet extends HttpServlet {
    @EJB
    MovieDAO movieDAO;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String size = request.getParameter("size");
        boolean small = !"large".equals(size);
        String id = request.getParameter("id");
        UpcomingMovie movieById = movieDAO.getMovieById(id);

        String fileName = id + ".jpg";
        String fileType = "image/jpg";

        response.setContentType(fileType);

        response.setHeader("Content-disposition", "filename=" + fileName);

        OutputStream out = response.getOutputStream();
        byte[] image = ImagesDAO.getImage(small ? movieById.getSmallImageName() : movieById.getLargeImageName());
        if (image != null) {
            out.write(image);
        }
        out.flush();
    }
}