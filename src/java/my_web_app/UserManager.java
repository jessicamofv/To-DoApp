/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_web_app;

import my_web_app.entities.AppUser;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 * <p>A simple managed bean to mediate between the user
 * and the persistence layer.</p>
 * @author rlubke
 */
// my annotations
@ManagedBean(name = "userManager")
@SessionScoped
public class UserManager {
    
    /**
     * <p>The key for the session scoped attribute holding the
     * appropriate <code>AppUser</code> instance.</p>
     */
    public static final String USER_SESSION_KEY = "user";
    
    @ManagedProperty(value = "#{appManager}")
    private AppManager appManager;
    
    /**
     * <p>The <code>PersistenceContext</code>.</p>
     */
    @PersistenceContext(unitName = "MyWebAppPU") 
    private EntityManager em;
    
    /**
     * <p>The transaction resource.</p>
     */
    @Resource 
    private UserTransaction utx;
    
    /**
     * <p>User properties.</p>
     */
    private String username;
    private String password;
    private String passwordc;
    private String fname;
    private String lname;
    
    // -------------------------------------------------------------- Properties
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPasswordc() {
        return passwordc;
    }
    
    public void setPasswordc(String passwordc) {
        this.passwordc = passwordc;
    }
    
    public String getFname() {
        return fname;
    }
    
    public void setFname(String fname) {
        this.fname = fname;
    }
    
    public String getLname() {
        return lname;
    }
    
    public void setLname(String lname) {
        this.lname = lname;
    }

    public AppManager getAppManager() {
        return appManager;
    }

    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }
    
    // ---------------------------------------------------------- Public Methods
    
    
    /**
     * <p>Validates the user.  If the user doesn't exist or the password
     * is incorrect, the appropriate message is added to the current
     * <code>FacesContext</code>.  If the user successfully authenticates,
     * navigate them to the page referenced by the outcome <code>main</code>.
     * </p>
     *
     * @return <code>main</code> if the user authenticates, otherwise
     *  returns <code>null</code>
     */
    public String validateUser() {   
        FacesContext context = FacesContext.getCurrentInstance();
        AppUser user = getUser();
        if (user != null) {
            if (!user.getPassword().equals(password)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                           "Login failed!",
                                           "The password specified is not correct.");
                context.addMessage(null, message);
                return null;
            }
            
            context.getExternalContext().getSessionMap().put(USER_SESSION_KEY, user);
            appManager.initApp(user);
            // my change
            return "main";
        } else {           
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Login failed!",
                    "Username '"
                    + username
                    +
                    "' does not exist.");
            context.addMessage(null, message);
            return null;
        }
    }
    
    /**
     * <p>Creates a new <code>AppUser</code>.  If the specified user name exists
     * or an error occurs when persisting the AppUser instance, enqueue a message
     * detailing the problem to the <code>FacesContext</code>.  If the 
     * user is created, move the user back to the login view.</p>
     *
     * @return <code>index</code> if the user is created, otherwise
     *  returns <code>null</code>
     */
    public String createUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        AppUser appUser = getUser();
        if (appUser == null) {
            if (!password.equals(passwordc)) {
                FacesMessage message = new FacesMessage("The specified passwords do not match.  Please try again");
                context.addMessage(null, message);
                return null;
            }
            appUser = new AppUser();
            appUser.setFirstName(fname);
            appUser.setLastName(lname);
            appUser.setUsername(username);
            appUser.setPassword(password);
            appUser.setScore(0);
            try {
                utx.begin();
                em.persist(appUser);
                utx.commit();
                // my change
                return "index";
            } catch (Exception e) {               
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                        "Error creating user!",
                                                        "Unexpected error when creating your account.  Please contact the system Administrator");
                context.addMessage(null, message);
                Logger.getAnonymousLogger().log(Level.SEVERE,
                                                "Unable to create new user",
                                                e);
                return null;
            }
        } else {           
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                    "Username '"
                                                      + username 
                                                      + "' already exists!  ",
                                                    "Please choose a different username.");
            context.addMessage(null, message);
            return null;
        }        
    }
    
    /**
     * <p>When invoked, it will invalidate the user's session
     * and move them to the login view.</p>
     *
     * @return <code>index</code>
     */
    public String logOut() {
        HttpSession session = (HttpSession)
             FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // my change
        return "index";
        
    }
    
    // --------------------------------------------------------- Private Methods
    
    
    /**
     * <p>This will attempt to lookup a <code>AppUser</code> object
     * based on the provided user name.</p>
     *
     * @return a <code>AppUser</code> object associated with the current
     *  username, otherwise, if no <code>AppUser</code> can be found,
     *  returns <code>null</code>
     */
    private AppUser getUser() {
        try {
            AppUser user = (AppUser)
            em.createNamedQuery("AppUser.findByUsername").
                    setParameter("username", username).getSingleResult();
            return user; 
        } catch (NoResultException nre) {
            return null;
        }
    }
   
}