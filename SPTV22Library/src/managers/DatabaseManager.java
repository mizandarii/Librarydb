/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entity.Book;
import entity.User;
import entity.History;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 *
 * @author admin
 */
public class DatabaseManager {
    private EntityManager em;

    public DatabaseManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SPTV22LibraryPU");
        this.em = emf.createEntityManager();
    }
    public void saveBook(Book book){
        try {
            em.getTransaction().begin();
            for (int i = 0; i < book.getAuthors().size(); i++) {
                if(book.getAuthors().get(i).getId() == null){
                    em.persist(book.getAuthors().get(i));
                }else{
                    em.merge(book.getAuthors().get(i));
                }
            }
            if(book.getId() == null){
                em.persist(book);
            }else{
                em.merge(book);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }
    public void saveUser(User user){
        try {
            em.getTransaction().begin();
                if(user.getReader().getId() == null){
                    em.persist(user.getReader());
                }else{
                    em.merge(user.getReader());
                }
            if(user.getId() == null){
                em.persist(user);
            }else{
                em.merge(user);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Пользователь не сохранен: "+e);
        }
    }

    public List<Book> getListBooks() {
        return em.createQuery("SELECT book FROM Book book").getResultList();
    }
    
    public void printListReadingBooks(DatabaseManager databaseManager){
        System.out.println("------Print list of all books being read");
        //List<History histories = databaseManager.
    }

    List<User> getListUsers() {
        return em.createQuery("SELECT user FROM User user").getResultList();
    }
    
    public List<Book> getReadingBooks(){
        return em.cresteQuery("SELECT history.book FROM History history WHERE history.returnBook = null").getResultList
    }

    public User authorization(String login, String password) {
        return (User) em.createQuery("SELECT user FROM User user WHERE user.login = :login AND user.password = :password")
                .setParameter("login", login)
                .setParameter("password", login)
                .getSingleResult();
    }
    
}