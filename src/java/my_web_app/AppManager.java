/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my_web_app;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import my_web_app.entities.AppUser;
import my_web_app.entities.ListItem;

/**
 *
 * @author Jessica
 */
@ManagedBean(name = "appManager")
@SessionScoped
public class AppManager implements Serializable {
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
    
    private AppUser currentUser;
    private String addImagePath;
    private String listItem;
    private ArrayList<String> listItems;
    private int itemNum;
    private HashMap<String, Integer> itemNums;
    private int itemIndex;
    private int maxItemsToDisplay;
    private boolean waitingForDifficulty;
    private HashMap<String, String> itemDifficulties;
    private String difficultyClass;
    private HashMap<String, Boolean> doneVisible;
    private HashMap<String, String> itemStatuses;
    private int score;
    private String viewingListDate;
    private String listHeader;
    private String hrClass;
    private static final int HOME_SCREEN = 0;
    private int homeScreen = HOME_SCREEN;
    private static final int MYPROGRESS_SCREEN = 1;
    private int myProgressScreen = MYPROGRESS_SCREEN;
    private int currentScreen;
    public boolean pastList;
    private String homeTabClass;
    private String myProgressTabClass;
    private boolean waitingOrPast;
    private HashMap<String, Long> addTimes;
    private int timeInterval;

    public String getAddImagePath() {
        return addImagePath;
    }

    public void setAddImagePath(String addImagePath) {
        this.addImagePath = addImagePath;
    }

    public String getViewingListDate() {
        return viewingListDate;
    }

    public void setViewingListDate(String viewingListDate) {
        this.viewingListDate = viewingListDate;
    }

    public String getListHeader() {
        return listHeader;
    }

    public void setListHeader(String listHeader) {
        this.listHeader = listHeader;
    }

    public String getHrClass() {
        return hrClass;
    }

    public void setHrClass(String hrClass) {
        this.hrClass = hrClass;
    }

    public int getHomeScreen() {
        return homeScreen;
    }

    public void setHomeScreen(int homeScreen) {
        this.homeScreen = homeScreen;
    }

    public int getMyProgressScreen() {
        return myProgressScreen;
    }

    public void setMyProgressScreen(int myProgressScreen) {
        this.myProgressScreen = myProgressScreen;
    }

    public String getHomeTabClass() {
        return homeTabClass;
    }

    public void setHomeTabClass(String homeTabClass) {
        this.homeTabClass = homeTabClass;
    }

    public String getMyProgressTabClass() {
        return myProgressTabClass;
    }

    public void setMyProgressTabClass(String myProgressTabClass) {
        this.myProgressTabClass = myProgressTabClass;
    }
    
    public String getListItem() {
        return listItem;
    }

    public void setListItem(String listItem) {
        this.listItem = listItem;
    }
    
    public ArrayList<String> getListItems() {
        return listItems;
    }

    public void setListItems(ArrayList<String> listItems) {
        this.listItems = listItems;
    }
    
    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    public int getMaxItemsToDisplay() {
        return maxItemsToDisplay;
    }

    public void setMaxItemsToDisplay(int maxItemsToDisplay) {
        this.maxItemsToDisplay = maxItemsToDisplay;
    }

    public boolean isWaitingForDifficulty() {
        return waitingForDifficulty;
    }

    public void setWaitingForDifficulty(boolean waitingForDifficulty) {
        this.waitingForDifficulty = waitingForDifficulty;
    }

    public String getDifficultyClass() {
        return difficultyClass;
    }

    public void setDifficultyClass(String difficultyClass) {
        this.difficultyClass = difficultyClass;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPastList() {
        return pastList;
    }

    public void setPastList(boolean pastList) {
        this.pastList = pastList;
    }

    public boolean isWaitingOrPast() {
        return waitingForDifficulty || pastList;
    }

    public void setWaitingOrPast(boolean waitingOrPast) {
        this.waitingOrPast = waitingOrPast;
    }
    
    public String getItemAndNum() {
        return "";
    }
    
    public void setItemAndNum(String itemAndNum)
    {
        if (itemAndNum.length() != 0)
        {
            String item = itemAndNum.substring(0, itemAndNum.indexOf("./."));
            int num = Integer.parseInt(itemAndNum.substring(itemAndNum.indexOf("./.") 
                    + 3));
            itemNums.put(item, num);

            ListItem lI = getListItemByItem(item.toLowerCase());
            lI.setItemNum(num);
            try {
                utx.begin();
                em.merge(lI);
                utx.commit();
            } catch (Exception e) {

            }
            
            ArrayList<String> reorderedListItems = new ArrayList();
            
            for (int i = 1; i <= listItems.size(); i++)
            {
                for (Object o : listItems)
                {
                    item = (String)o;
                    if (itemNums.get(item) == i)
                    {
                        reorderedListItems.add(item);
                    }
                }
            }
            
            listItems.clear();
            
            for (int i = 0; i < reorderedListItems.size(); i++)
                listItems.add(reorderedListItems.get(i));
        }
    }
    
    public String getTimeInterval() {
        String timeIntervalString = "" + timeInterval;
        return timeIntervalString;
    }
    
    public void setTimeInterval(String lowercaseItem) {
        long addTime = addTimes.get(lowercaseItem);
        long currentTime = new GregorianCalendar().getTimeInMillis();
        timeInterval = (int)((currentTime - addTime)/60000);
    }
    
    public void initApp(AppUser user)
    {
        currentUser = user;
        currentScreen = homeScreen;
        homeTabClass = "active";
        myProgressTabClass = "notActive";
        viewingListDate = getDateString();
        listHeader = "Today's To-Do List";
        hrClass = "col-xs-3";
        clearData();
        waitingForDifficulty = false;
        addImagePath = "./resources/images/Add.png";
        score = currentUser.getScore();
        
        checkForExpiredItems();
        
        List todaysList = getDaysList();
        if (todaysList != null && !todaysList.isEmpty())
            loadList(todaysList);
    }
    
    public void clearData()
    {
        listItem = "";
        listItems = new ArrayList();
        itemNum = 0;
        itemNums = new HashMap();
        itemIndex = 0;
        maxItemsToDisplay = 0;
        itemDifficulties = new HashMap();
        difficultyClass = "notWaiting";
        doneVisible = new HashMap();
        itemStatuses = new HashMap();
        addTimes = new HashMap();
        timeInterval = 0;
    }
    
    public void checkForExpiredItems()
    {
        List expiredItems = getExpiredItems();
        
        if (expiredItems != null && !expiredItems.isEmpty())
        {
            for (Object o : expiredItems)
            {
                ListItem lI = (ListItem)o;

                if (!(lI.getListDate().equals(getDateString())))
                {
                    markAsExpired(lI);
                }
            }
        }
    }
    
    public void loadList(List daysList)
    {
        for (int i = 1; i <= daysList.size(); i++)
        {
            for (Object o : daysList)
            {
                ListItem lI = (ListItem)o;
                if (lI.getItemNum() == i)
                {
                    listItems.add(lI.getItem());
                    maxItemsToDisplay++;
                    itemNum = lI.getItemNum();
                    itemNums.put(lI.getItem().toLowerCase(), lI.getItemNum());
                    itemDifficulties.put(lI.getItem().toLowerCase(), lI.getDifficulty());
                    addTimes.put(lI.getItem().toLowerCase(), lI.getAddTime());
                    itemStatuses.put(lI.getItem().toLowerCase(), lI.getStatus());
                    doneVisible.put(lI.getItem().toLowerCase(), false);
                }
            }
        }
    }
    
    public void goToPreviousDaysList() throws ParseException
    {
        viewingListDate = getPreviousDayDateString();
        listHeader = "To-Do List";
        hrClass = "col-xs-2";
        pastList = true;
        addImagePath = "./resources/images/AddDisabled.png";
        clearData();
        List previousDaysList = getDaysList();
        
        if (previousDaysList != null && !previousDaysList.isEmpty())
            loadList(previousDaysList);
    }
    
    public void goToNextDaysList() throws ParseException
    {
        if (!viewingListDate.equals(getDateString()))
        {
            viewingListDate = getNextDayDateString();
            
            if (!viewingListDate.equals(getDateString()))
            {
                listHeader = "To-Do List";
                hrClass = "col-xs-2";
                pastList = true;
                addImagePath = "./resources/images/AddDisabled.png";
            }
            else
            {
                listHeader = "Today's To-Do List";
                hrClass = "col-xs-3";
                pastList = false;
                addImagePath = "./resources/images/Add.png";
            }
            
            clearData();
            List nextDaysList = getDaysList();
            
            if (nextDaysList != null && !nextDaysList.isEmpty())
                loadList(nextDaysList);
        }
    }
    
    public boolean isViewingHomeScreen()
    {
        return currentScreen == homeScreen;
    }
    
    public boolean isViewingMyProgressScreen()
    {
        return currentScreen == myProgressScreen;
    }
    
    public void switchToScreen(int screen)
    {
        currentScreen = screen;
        
        if (screen == HOME_SCREEN)
        {
            myProgressTabClass = "notActive";
            homeTabClass = "active";
        }
        
        if (screen == MYPROGRESS_SCREEN)
        {
            homeTabClass = "notActive";
            myProgressTabClass= "active";
        }
    }
    
    public void doPreAddChecks()//addToList()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        if (listItem.length() == 0)
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "",
                                    "Please enter an item.");
            context.addMessage(null, message);
        }
        else
        {
            String lowercaseItem = listItem.toLowerCase();
            String lowercaseItemWithoutPeriod = lowercaseItem.replace(".", "");
            String lowercaseItemWithPeriod = lowercaseItem + "."; 
            if (isInList(lowercaseItemWithoutPeriod) || isInList(lowercaseItemWithPeriod))
            {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "",
                                        "That item is already on the list.");
                context.addMessage(null, message);
            }
            else
            {
                waitingForDifficulty = true;
                addImagePath = "./resources/images/AddDisabled.png";
                difficultyClass = "waiting";
            }
        }
    }
    
    public boolean isInList(String item)
    {
        for (String lI : listItems)
        {
            if (item.equals(lI.toLowerCase()))
                return true;
        }
        
        return false;
    }
    
    public void addToList(String difficultyLevel)//assignDifficulty(String difficultyLevel)
    {
        listItems.add(listItem);
        itemNum++;
        itemNums.put(listItem.toLowerCase(), itemNum);
        maxItemsToDisplay++;
        waitingForDifficulty = false;
        itemDifficulties.put(listItem.toLowerCase(), difficultyLevel);
        addImagePath = "./resources/images/Add.png";
        difficultyClass = "notWaiting";
        long currentTime = new GregorianCalendar().getTimeInMillis();
        addTimes.put(listItem.toLowerCase(), currentTime); 
        doneVisible.put(listItem.toLowerCase(), false);
        itemStatuses.put(listItem.toLowerCase(), "notDone");
        ListItem item = new ListItem();
        item.setItem(listItem);
        item.setListDate(getDateString());
        item.setItemNum(itemNum);
        item.setDifficulty(difficultyLevel);
        item.setAddTime(currentTime);
        item.setStatus("notDone");
        item.setAppUserId(currentUser);
        try {
            utx.begin();
            em.persist(item);
            utx.commit();
        } catch (Exception e) {
            
        }

        listItem = "";
    }
        
    public int getItemNum(String lowercaseItem)
    {
        return itemNums.get(lowercaseItem);
    }
    
    public String getNumId(String lowercaseItem)
    {
        int num = itemNums.get(lowercaseItem);
        String numId = "num" + num;
        return numId;
    }
    
    public String getItemId(String lowercaseItem)
    {
        int num = itemNums.get(lowercaseItem);
        String itemId = "item" + num;
        return itemId;
    }
    
    /*public String getHiddenId(String lowercaseItem)
    {
        int num = itemNums.get(lowercaseItem);
        String hiddenId = "hidden" + num;
        return hiddenId;
    }*/
    
    public String getDifficultyClass(String lowercaseItem)
    {
        return itemDifficulties.get(lowercaseItem);
    }
    
    public void showDone(String lowercaseItem)
    {
        if (!itemStatuses.get(lowercaseItem).equals("done"))
            doneVisible.put(lowercaseItem, true);
    }
    
    public void hideDone(String lowercaseItem)
    {
        doneVisible.put(lowercaseItem, false);
    }
    
    public boolean getDoneVisibility(String lowercaseItem)
    {
        return doneVisible.get(lowercaseItem);
    }
    
    public String getItemStatus(String lowercaseItem)
    {
        return itemStatuses.get(lowercaseItem);
    }
    
    public void markAsDone(String lowercaseItem)
    {
        //doneVisible.put(lowercaseItem, false);
        
        if (viewingListDate.equals(getDateString()))
        {
            if (itemStatuses.get(lowercaseItem).equals("notDone"))
            {
                itemStatuses.put(lowercaseItem, "done");

                if (itemDifficulties.get(lowercaseItem).equals("easy"))
                    score += 5;
                else if (itemDifficulties.get(lowercaseItem).equals("medium"))
                    score += 10;
                else
                    score += 20;

                ListItem currentItem = getListItemByItem(lowercaseItem);
                currentItem.setStatus("done");
                try {
                    utx.begin();
                    em.merge(currentItem);
                    utx.commit();
                } catch (Exception e) {

                }
            }
            else if (itemStatuses.get(lowercaseItem).equals("done"))
            {
                itemStatuses.put(lowercaseItem, "notDone");

                if (itemDifficulties.get(lowercaseItem).equals("easy"))
                    score -= 5;
                else if (itemDifficulties.get(lowercaseItem).equals("medium"))
                    score -= 10;
                else
                    score -= 20;

                ListItem currentItem = getListItemByItem(lowercaseItem);
                currentItem.setStatus("notDone");
                try {
                    utx.begin();
                    em.merge(currentItem);
                    utx.commit();
                } catch (Exception e) {

                }
            }

            saveScore();
        }
    }
    
    public void markAsExpired(ListItem lI)
    {
        if ((lI.getDifficulty()).equals("easy"))
            score -= 20;
        else if ((lI.getDifficulty()).equals("medium"))
            score -= 10;
        else
            score -= 5;
        
        saveScore();
        
        lI.setStatus("expired");
        try {
            utx.begin();
            em.merge(lI);
            utx.commit();
        } catch (Exception e) {
                
        }
    }
    
    public void saveScore()
    {
        currentUser.setScore(score);
        try {
            utx.begin();
            em.merge(currentUser);
            utx.commit();
        } catch (Exception e) {
            
        }
    }
    
    /*public void updateItemNums()
    {
        if (itemNums != null && !itemNums.isEmpty())
        {
            Set<String> items = itemNums.keySet();
            
            for (Object o : items)
            {
                String item = (String)o;
                int num = itemNums.get(item);
                
                ListItem lI = getListItemByItem(item.toLowerCase());
                lI.setItemNum(num);
                try {
                    utx.begin();
                    em.persist(lI);
                    utx.commit();
                } catch (Exception e) {

                }
            }
        }
    }*/
    
    public String getDateString()
    {
        Date date = new Date();
        String dateString = new SimpleDateFormat("EEE, MMM d, yyyy").format(date);
        return dateString;
    }
    
    // show the list from the day before the day currently being viewed
    public String getPreviousDayDateString() throws ParseException
    {
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        Date date = dateFormat.parse(viewingListDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(cal.DATE, -1);
        return dateFormat.format(cal.getTime());  
    }
    
    // show the list from the day after the day currently being viewed
    public String getNextDayDateString() throws ParseException
    {
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        Date date = dateFormat.parse(viewingListDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(cal.DATE, 1);
        return dateFormat.format(cal.getTime());
    }
    
    private ListItem getListItemByItem(String lowercaseItem)
    {
        List itemsList = getDaysList();
        
        if (itemsList != null && !itemsList.isEmpty())
        {
            for (Object o : itemsList)
            {
                ListItem lI = (ListItem)o;

                if (((lI.getItem()).toLowerCase()).equals(lowercaseItem))
                {
                    return lI;
                }
            }
        }
        
        return null;
    }
    
    private List getDaysList()
    {
        try {
            List todaysList =
            em.createNamedQuery("ListItem.findByListDate").
                    setParameter("appUserId", currentUser.getId()).
                    setParameter("listDate", viewingListDate).getResultList();
            return todaysList; 
        } catch (NoResultException nre) {
            return null;
        }
    }
    
    private List getExpiredItems()
    {
        try {
            List expiredItems = 
            em.createNamedQuery("ListItem.findByStatus").
                    setParameter("appUserId", currentUser.getId()).
                    setParameter("status", "notDone").getResultList();
            return expiredItems;
        } catch (NoResultException nre) {
            return null;
        }
    }
}