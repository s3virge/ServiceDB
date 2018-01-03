package core.utils;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is a TextField which implements an "autocomplete" functionality, based on a supplied list of entries.
 * @author Caleb Brinkman
 */
public class AutoCompleteTextField extends TextField
{
    /** The existing autocomplete entries. */
    private final TreeSet<String> entries;
    /** The popup used to select an entry. */
    private ContextMenu entriesPopup;

    /** Construct a new AutoCompleteTextField. */
    public AutoCompleteTextField() {
        super();

        /**
         Обобщенный класс TreeSet<E> представляет структуру данных в виде дерева, в котором все объекты хранятся в
         отсортированном виде по возрастанию.
         TreeSet является наследником класса AbstractSet и реализует интерфейс NavigableSet.*/
        entries = new TreeSet<>();
        entriesPopup = new ContextMenu();

        /** ловим изменения в поле ввода*/
        textProperty().addListener((observable, oldValue, newValue) -> {
            //если текста в поле ввода нет
            if (getText().length() == 0) {
                //то не показываем выпадающее меню
                entriesPopup.hide();
            }
            else {

                /*searchResult.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));*/

                /**
                 * Stream API это новый способ работать со структурами данных в функциональном стиле.
                 * Чаще всего с помощью stream в Java 8 работают с коллекциями, но на самом деле этот механизм может
                 * использоваться для самых различных данных.
                 * https://habrahabr.ru/company/luxoft/blog/270383/
                 * */

                //String реалезует интерфей CharSequence. getText возвращает String
                //получаем введенный в поле ввода текст. Делаем все буктвы прописными
                CharSequence charSequence = getText().toLowerCase();
                //превращаем список автозамены в стрим
                Stream <String> entriesStream = entries.stream();
                //filter Отфильтровывает записи, возвращает только записи, соответствующие условию
                Stream <String> filteredEntriesStream = entriesStream.filter(s -> s.toLowerCase().contains(charSequence));
                //collect	Представление результатов в виде коллекций и других структур данных
                // collection.stream().filter((s) -> s.contains(«1»)).collect(Collectors.toList())
                final List <String> filteredEntries = filteredEntriesStream.collect(Collectors.toList());

                if (entries.size() > 0) {
                    populatePopup(filteredEntries);

                    if (!entriesPopup.isShowing()) {
                        entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                    }
                }
                else {
                    entriesPopup.hide();
                }
            }
        });

        /** наверное отлавливае изменение фокуса ввода*/
        focusedProperty().addListener( (observable, oldValue, newValue) -> entriesPopup.hide());
    }

    /**
     * Get the existing set of autocomplete entries.
     * @return The existing autocomplete entries.
     */
    public SortedSet<String> getEntries() { return entries; }

    /**
     * Populate the entry set with the given search results.  Display is limited to 10 entries, for performance.
     * @param searchResult The set of matching strings.
     *
     * Заполняем всплывающее меню данными
     */
    private void populatePopup(List<String> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        // If you'd like more entries, modify this line.
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);

        for (int i = 0; i < count; i++)
        {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            CustomMenuItem menuItem = new CustomMenuItem(entryLabel, true);

            menuItem.setOnAction(event -> {
                setText(result);
                entriesPopup.hide();
            });

            menuItems.add(menuItem);
        }

        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }
}