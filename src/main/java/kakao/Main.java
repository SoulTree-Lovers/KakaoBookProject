package kakao;

import java.util.List;
import java.util.Scanner;
import kakao.model.Book;
import kakao.util.KakaoBookAPI;
import kakao.util.PDFGenerator;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("도서 제목을 입력하세요: ");
            String bookTitle = scanner.nextLine();
            List<Book> books = KakaoBookAPI.searchBooks(bookTitle);

            if (books.isEmpty()) {
                System.out.println("검색 결과가 없습니다.");
            } else {
                for (Book book : books) {
                    System.out.println(book);
                }
                String fileName = "books.pdf";
                PDFGenerator.generateBookListPDF(books, fileName);
                System.out.println(fileName + " 파일이 생성되었습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
