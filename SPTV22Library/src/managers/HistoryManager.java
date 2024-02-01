/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entity.Book;
import entity.History;
import entity.Reader;
import entity.User;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import sptv22library.App;
import tools.InputProtection;

/**
 *
 * @author admin
 */
public class HistoryManager {

    private final Scanner scanner;
    private final BookManager bookManager;
    private final ReaderManager readerManager;

    public HistoryManager(
            Scanner scanner, 
            ReaderManager readerManager, 
            BookManager bookManager) {
        this.scanner = scanner;
        this.bookManager = bookManager;
        this.readerManager = readerManager;
    }
    /**
     * Логика работы метода
     * 1. список книг
     * 2. пользователь вводит номер книги
     * 3. записываем в историю выбранную книгу из массива книг
     * 4. список читателей
     * 5. пользователь вводит номер читателя
     * 6. записываем в историю выбранного читателя из массива читателей
     * 7. записываем в историю дату выдачи книги
     * @param books
     * @param readers
     * @return History history
     */
    public History takeOutBook(List<Book> books) {
        History history = new History();
        bookManager.printListBooks(books);
        System.out.print("Enter number book from list: ");
        int numberBook = InputProtection.intInput(1, books.size()); //scanner.nextInt(); scanner.nextLine();
        if(books.get(numberBook - 1).getCount() > 0){
            books.get(numberBook - 1).setCount(books.get(numberBook - 1).getCount() - 1);
            history.setBook(books.get(numberBook - 1));
            history.setUser(App.user);
            history.setTakeOutBook(new GregorianCalendar().getTime());
            return history;
        }else{
            System.out.println("All books are taken");
            return null;
        }
    }

    public void printListReadingBooks(List<History> histories) {
        System.out.println("----- List reading books -----");
        for (int i = 0; i < histories.size(); i++) {
            if(histories.get(i).getReturnBook() == null){
                System.out.printf("%d. %s. read %s %s%n",
                        i+1,
                        histories.get(i).getBook().getTitle(),
                        histories.get(i).getUser().getReader().getFirstname(),
                        histories.get(i).getUser().getReader().getLastname()
                );
            }
        }
    }

    public void returnBook(List<History> histories) {
        System.out.println("Return book:");
        this.printListReadingBooks(histories);
        System.out.println("Enter number book: ");
        int numberReturnBook = InputProtection.intInput(1, histories.size());
        if(histories.get(numberReturnBook - 1).getBook().getCount() 
                < histories.get(numberReturnBook - 1).getBook().getQuantity()){
            histories.get(numberReturnBook - 1).getBook().setCount(histories.get(numberReturnBook - 1).getBook().getCount() + 1);
            histories.get(numberReturnBook - 1).setReturnBook(new GregorianCalendar().getTime());
            
        }else{
            System.out.println("All copies already in stock");
        }
    }

    public void bookRating(List<History> histories) {
        Map<Book,Integer> mapRatingBook = new HashMap<>();
        for (int i = 0; i < histories.size(); i++) {
            if(mapRatingBook.containsKey(histories.get(i).getBook())){
                mapRatingBook.put(histories.get(i).getBook(), mapRatingBook.get(histories.get(i).getBook())+1);
            }else{
                mapRatingBook.put(histories.get(i).getBook(),1);
            }
        }
        Map<Book, Integer> sortedMapRatingBook = mapRatingBook.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        int n = 1;
        for (Map.Entry<Book, Integer> entry : sortedMapRatingBook.entrySet()) {
            System.out.printf("%d. %s: %d%n",
                    n,
                    entry.getKey().getTitle(),
                    entry.getValue()
            );
            n++;
        }
    }
    
}