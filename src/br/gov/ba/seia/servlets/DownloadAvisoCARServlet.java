package br.gov.ba.seia.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class DownloadAvisoCARServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream inputStream = (InputStream) request.getSession().getAttribute("arquivo");
		request.getSession().setAttribute("arquvo",null);
		response.reset();
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition","attachment;filename=Avisos_CAR_BNDES_INEMA.zip");
		OutputStream out = response.getOutputStream();
		out.write(IOUtils.toByteArray(inputStream));
		out.close();
	}
}
