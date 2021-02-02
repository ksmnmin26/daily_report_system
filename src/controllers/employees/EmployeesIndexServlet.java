package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesIndexServlet
 */
@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em=DBUtil.createEntityManager();

        //開くページ数を取得
        int page=1;
        try{
            page=Integer.parseInt(request.getParameter("page"));
        }catch(NumberFormatException e){}

        //最大件数と開始位置を指定して従業員情報を取得
        List<Employee>employees=em.createNamedQuery("getAllEmployees",Employee.class)
                                .setFirstResult(15*(page-1))//何件目から取得するか
                                .setMaxResults(15)//最大取得数
                                .getResultList();//問い合わせ結果を取得

        //従業員情報の全情報を取得
        long employees_count=(long)em.createNamedQuery("getEmployeesCount",Long.class)
                                .getSingleResult();


    em.close();

    request.setAttribute("employees",employees);
    request.setAttribute("employees_count",employees_count);
    request.setAttribute("page",page);

    if(request.getSession().getAttribute("flush")!=null){
        request.setAttribute("flush",request.getSession().getAttribute("flush"));
        request.getSession().removeAttribute("flush");
    }

    RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
    rd.forward(request,response);
    }

}
