package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory addressBookModel of the address book data.
 */
public class AddressBookModelManager extends ComponentManager implements AddressBookModel {
    private static final Logger logger = LogsCenter.getLogger(AddressBookModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a AddressBookModelManager with the given addressBook and userPrefs.
     */
    public AddressBookModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        // this.filteredPersons = new FilteredList<>(addressBook.getAllPersonList());
        this.filteredPersons = new FilteredList<>(addressBook.getPersonList());
    }

    public AddressBookModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    /**
     * Checks for the difference between existing address book and given address book, then soft-deletes/adds the
     * appropriate entries.
     * @param newData new address book to be compared with the existing one.
     */
    // TODO: Actually compare the two address books rather than override the old address book with the new one.
    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the addressBookModel has changed. */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code AddressBook} (was {@code versionedAddressBook}).
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================
    /*
    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public void undoAddressBook() {
        addressBook.undo();
        indicateAddressBookChanged();
    }

    @Override
    public void redoAddressBook() {
        addressBook.redo();
        indicateAddressBookChanged();
    }
    @Override
    public void commitAddressBook() {
        addressBook.commit();
    }
    */

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof AddressBookModelManager)) {
            return false;
        }

        // state check
        AddressBookModelManager other = (AddressBookModelManager) obj;
        return addressBook.equals(other.addressBook);
    }

}