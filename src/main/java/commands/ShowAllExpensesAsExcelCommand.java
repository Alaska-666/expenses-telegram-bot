package commands;

import database.Database;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowAllExpensesAsExcelCommand extends ServiceCommand {
    private final Database database;
    private final String[] HEADERS = {"Дата", "Наименование", "Сумма", "Кто платил", "Чья трата", "Статус"};

    public ShowAllExpensesAsExcelCommand(String identifier, String description, Database database) {
        super(identifier, description);
        this.database = database;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            absSender.execute(createSendDocument(chat.getId()));
        } catch (IOException | TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createHeader(Workbook workbook, Sheet sheet) {
        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setWrapText(true);

        for (int i = 0; i < HEADERS.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(HEADERS[i]);
            headerCell.setCellStyle(headerStyle);
        }
    }

    private File getExcelFile() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("expenses");
            createHeader(workbook, sheet);
            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);
            database.addExpensesToExcelTable(sheet, style, 1);
            String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
            File outputFile = File.createTempFile(String.format("Expenses_%s_", date), ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                workbook.write(fos);
            }
            return outputFile;
        }
    }

    private SendDocument createSendDocument(Long chatId) throws IOException {
        SendDocument document = new SendDocument();
        document.setChatId(chatId.toString());
        document.setDocument(new InputFile(getExcelFile()));
        return document;
    }
}
