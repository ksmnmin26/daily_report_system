package filters;
//ログイン状態のチェックはサーブレットの処理よりも前に実行される
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import models.Employee;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

       String context_path=((HttpServletRequest)request).getContextPath();
       String servlet_path=((HttpServletRequest)request).getServletPath();

       if(!servlet_path.matches("/css.*")){ //CSSフォルダ内は認証処理(ログインしているかのチェック)から除外する
           HttpSession session=((HttpServletRequest)request).getSession();

           //セッションスコープに保存された従業員（ログインユーザ）情報を取得
           Employee e=(Employee)session.getAttribute("login_employee");//ログアウトのときeはnullになる

           if(!servlet_path.equals("login")){ //ログイン画面以外について
               //ログアウトしている状態であれば
               //ログイン画面にリダイレクト
               if(e==null){
                   ((HttpServletResponse)response).sendRedirect(context_path+"/login");
                   return;
               }

               //従業員管理者機能は管理者のみが閲覧できるようにする
               if(servlet_path.matches("/employees.*")&&e.getAdmin_flag()==0){//0=一般なら
                   ((HttpServletResponse)response).sendRedirect(context_path+"/");//トップページへ
                   return;
               }
           }else{  //ログイン画面について
               //ログインしているのにログイン画面を表示させようとした場合は
               //システムのトップページにリダイレクト

              if(e!=null){
                   ((HttpServletResponse)response).sendRedirect(context_path+"/");
                   return;
              }

           }


       }

    chain.doFilter(request, response);
}

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
