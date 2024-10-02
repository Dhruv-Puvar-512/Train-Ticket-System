package controller;

import DAO.TrainCrud;
import models.Train;
import services.TrainServices;
import models.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/train")
public class TrainServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Customer customer = (Customer) session.getAttribute("customer");

		if (session == null || customer == null) {
			response.sendRedirect("login.jsp");
			return;
		}

		boolean isAdmin = "true".equals(customer.isAdmin());
		String action = request.getParameter("action");

		switch (action) {
		case "register":
			if (isAdmin) {
				handleTrainRegister(request, response, session);
			} else {
				session.setAttribute("error", "Unauthorized access!");
				response.sendRedirect("trainRegister.jsp");
			}
			break;
		case "update": // POST request to actually update the train
			if (isAdmin) {
				try {
					handleTrainUpdatePost(request, response, session);
				} catch (Exception e) {
					e.printStackTrace();
					session.setAttribute("error", "Error during train update: " + e.getMessage());
					response.sendRedirect("errorPage.jsp");
				}
			} else {
				session.setAttribute("error", "Unauthorized access!");
				response.sendRedirect("homePage.jsp");
			}
			break;
		case "delete":
			if (isAdmin) {
				handleTrainDelete(request, response, session);
			} else {
				session.setAttribute("error", "Unauthorized access!");
				response.sendRedirect("viewAllTrain.jsp");
			}
			break;
			
		case "bookTrain":
	            int trainId = Integer.parseInt(request.getParameter("trainId"));
	            session.setAttribute("success", "Booking successful for Train ID: " + trainId);
	            response.sendRedirect("bookTicket.jsp");

		case "viewSpecificTrain":
			handleViewSpecificTrain(request, response, session);
		default:
			session.setAttribute("error", "Invalid action!");
			response.sendRedirect("errorPage.jsp");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Customer customer = (Customer) session.getAttribute("customer");

		if (session == null || customer == null) {
			response.sendRedirect("loginPage.jsp");
			return;
		}

		String action = request.getParameter("action");

		switch (action) {
		case "viewAll":
			if ("true".equals(customer.isAdmin())) {
				handleViewAllTrains(request, response, session);
			} else {
				session.setAttribute("error", "Unauthorized access!");
				response.sendRedirect("viewAllTrain.jsp");
			}
			break;
		case "viewSpecificTrain":
			handleViewSpecificTrain(request, response, session);
			break;
		case "update": // GET request to prefill the form with train data
			if ("true".equals(customer.isAdmin())) {
				try {
					handleTrainUpdateGet(request, response, session);
				} catch (Exception e) {
					e.printStackTrace();
					session.setAttribute("error", "Error fetching train details: " + e.getMessage());
					response.sendRedirect("errorPage.jsp");
				}
			} else {
				session.setAttribute("error", "Unauthorized access!");
				response.sendRedirect("homePage.jsp");
			}
			break;
		default:
			session.setAttribute("error", "Invalid action!");
			response.sendRedirect("errorPage.jsp");
		}
	}

	private void handleTrainRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {

		String trainName = request.getParameter("trainName");
		String trainDept = request.getParameter("trainDept");
		String trainArr = request.getParameter("trainArr");
		String trainDeptTime = request.getParameter("trainDeptTime");
		String trainArrTime = request.getParameter("trainArrTime");
		String trainDate = request.getParameter("trainDate");
		int capacity = Integer.parseInt(request.getParameter("capacity"));
		String trainRoute = request.getParameter("trainRoute");
		String trainTier = request.getParameter("trainTier");
		int trainPrice = Integer.parseInt(request.getParameter("trainPrice"));

		TrainServices trainServices = new TrainServices();
		Train train = TrainServices.registerTrain(trainName, trainDept, trainArr, trainDeptTime, trainArrTime,
				trainDate, capacity, trainRoute, trainTier, trainPrice);

		if (train != null) {
			session.setAttribute("success", "Train registered successfully!");
			response.sendRedirect("trainRegister.jsp");
		} else {
			System.out.print("sdsd");
			session.setAttribute("error", trainServices.errorMessage);
			response.sendRedirect("trainRegister.jsp");
		}
	}

	// GET handler for pre-filling the update form
	private void handleTrainUpdateGet(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException, ServletException, SQLException {
		int trainId = Integer.parseInt(request.getParameter("trainId"));
		Train train = TrainCrud.getTrainById(trainId);

		if (train != null) {
			// Set the train object in the request to prefill the form
			request.setAttribute("train", train);
			request.getRequestDispatcher("trainUpdate.jsp").forward(request, response);
		} else {
			session.setAttribute("error", "Train not found!");
			response.sendRedirect("viewAllTrain.jsp");
		}
	}

	// POST handler for updating the train after form submission
	private void handleTrainUpdatePost(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException, SQLException {
		TrainServices trainServices = new TrainServices();

		int trainId = Integer.parseInt(request.getParameter("trainId"));
		String trainName = request.getParameter("trainName");
		String trainDept = request.getParameter("trainDept");
		String trainArr = request.getParameter("trainArr");
		String trainDeptTime = request.getParameter("trainDeptTime");
		String trainArrTime = request.getParameter("trainArrTime");
		String trainDate = request.getParameter("trainDate");
		int capacity = Integer.parseInt(request.getParameter("capacity"));
		String trainRoute = request.getParameter("trainRoute");
		String trainTier = request.getParameter("trainTier");
		int trainPrice = Integer.parseInt(request.getParameter("trainPrice"));

		// Call the update train service method
		Train updatedTrain = TrainServices.updateTrain(trainId, trainName, trainDept, trainArr, trainDeptTime,
				trainArrTime, trainDate, capacity, trainRoute, trainTier, trainPrice);

		if (updatedTrain != null) {
			System.out.print("dsdsd");
			session.setAttribute("success", "Train updated successfully!");
			response.sendRedirect("viewAllTrain.jsp");
		} else {
			session.setAttribute("error", trainServices.errorMessage);
			response.sendRedirect("trainUpdate.jsp?trainId=" + trainId);
		}
	}

	private void handleTrainDelete(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		int trainId = Integer.parseInt(request.getParameter("trainId"));
		TrainServices trainServices = new TrainServices();
		boolean result = trainServices.deleteTrain(trainId);

		if (result) {
			session.setAttribute("success", "Train deleted successfully!");
			response.sendRedirect("viewAllTrain.jsp");
		} else {
			session.setAttribute("error", trainServices.errorMessage);
			response.sendRedirect("viewAllTrain.jsp");
		}
	}

	private void handleViewAllTrains(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		TrainServices trainServices = new TrainServices();
		try {
			ArrayList<Train> trains = trainServices.displayAllTrains();
			session.setAttribute("trainList", trains);
			response.sendRedirect("viewAllTrain.jsp");
		} catch (Exception e) {
			session.setAttribute("error", "Failed to fetch trains.");
			response.sendRedirect("errorPage.jsp");
		}
	}

	private void handleViewSpecificTrain(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		String trainDept = request.getParameter("trainDept");
		String trainArr = request.getParameter("trainArr");

		TrainServices trainServices = new TrainServices();
		System.out.print("sdsdsdd");
		try {
			ArrayList<Train> trains = trainServices.searchAndDisplayTrains(trainDept, trainArr);
			session.setAttribute("train", trains);
			response.sendRedirect("searchTrain.jsp");
		} catch (Exception e) {
			session.setAttribute("error", "Failed to fetch specific train.");
			response.sendRedirect("errorPage.jsp");
		}
	}
}
