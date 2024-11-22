package com.example.test.views;

import com.example.test.entity.ContactEntity;
import com.example.test.service.ContactService;
import com.example.test.service.ExportService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("")
public class HomeView extends VerticalLayout {
    @Autowired
    private ContactService contactService;
    @Autowired
    private ExportService exportService;
    private Grid<ContactEntity> grid;

    public HomeView() {
    }

    @PostConstruct
    public void init() {
        getElement().getStyle().set("background-color", "rgba(220, 220, 220, 1)");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayout.setHeight("100vh");
        verticalLayout.add(new H1("Справочник"), new Button("Показать справочник", event -> openContactListDialog()));
        add(verticalLayout);
    }

    private void openContactListDialog() {
        List<ContactEntity> all = contactService.findAll();


        Dialog dialog = new Dialog();
        dialog.setWidth("80%");
        dialog.setHeight("80%");

        Button closeButton = new Button("Закрыть", e -> dialog.close());
        Button create = new Button("Добавить", e -> editContactDialog(null));
        Button edit = new Button("Изменить", e -> editContactDialog(grid.getSelectedItems().iterator().next()));
        Button export = new Button("Экспорт", e -> {
            StreamResource streamResource = exportService.exportPdf(grid.getSelectedItems().iterator().next());
            StreamRegistration streamRegistration = VaadinSession.getCurrent().getResourceRegistry().registerResource(streamResource);
            UI.getCurrent().getPage().executeJs("window.open($0, '_blank')", streamRegistration.getResourceUri().toString());
        });


        edit.setEnabled(false);
        export.setEnabled(false);
        grid = new Grid<>(ContactEntity.class);
        grid.setHeight("90%");
        grid.setColumns("name", "phone", "email");
        grid.getColumnByKey("name").setHeader("ФИО");
        grid.getColumnByKey("phone").setHeader("Телефон");
        grid.getColumnByKey("email").setHeader("Почта");
        grid.setItems(all);
        grid.addItemDoubleClickListener(e -> {
            editContactDialog(e.getItem());
        });
        grid.addSelectionListener(e -> {
            edit.setEnabled(!grid.getSelectedItems().isEmpty());
            export.setEnabled(!grid.getSelectedItems().isEmpty());
        });


        dialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                updateGridData(contactService);
            }
        });
        dialog.add(new HorizontalLayout(create, edit, export), grid, closeButton);
        dialog.open();
    }

    private void editContactDialog(ContactEntity contact) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(contact == null ? "Добавление" : "Изменение");
        TextField name = new TextField();
        name.setLabel("ФИО");
        TextField phone = new TextField();
        phone.setLabel("Телефон");
        TextField email = new TextField();
        email.setLabel("Почта");
        String id;

        if (contact != null) {
            id = contact.getId();
            name.setValue(contact.getName());
            phone.setValue(contact.getPhone());
            email.setValue(contact.getEmail());
        } else {
            id = null;
        }

        Button saveButton = new Button("Сохранить", e -> {
            contactService.save(new ContactEntity(id, name.getValue(), phone.getValue(), email.getValue()));
            Notification.show("Данные сохранены!");
            dialog.close();
        });
        Button closeButton = new Button("Закрыть", e -> dialog.close());
        VerticalLayout layout = new VerticalLayout(name, phone, email, new HorizontalLayout(saveButton, closeButton));
        dialog.add(layout);
        dialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                updateGridData(contactService);
            }
        });
        dialog.open();
    }

    private void updateGridData(ContactService contactService) {
        grid.setItems(contactService.findAll());
    }
}
