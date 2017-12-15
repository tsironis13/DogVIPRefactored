package com.dogvip.giannis.dogviprefactored.config;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.dogvip.giannis.dogviprefactored.BuildConfig;
import com.dogvip.giannis.dogviprefactored.R;
import com.dogvip.giannis.dogviprefactored.utilities.errorhandling.throwables.NoOnwerExistsException;
import com.dogvip.giannis.dogviprefactored.utilities.errorhandling.throwables.StatusErrorException;

/**
 * Created by giannis on 4/11/2017.
 */

public class AppConfig {

    public static final String BASE_URL = BuildConfig.BASE_URL;
    //SUBJECT TYPES
    public static final int PUBLISH_SUBJ = 0;
    //APPLICATION EVENTS
    public static final int FRAGMENT_ANIMATION = 1000;
    public static final int TOKEN_IS_READY = 1030;
    //back button pressed
    public static final int KEYCODE_BACK = 4;
    //JOB PRIORITY
    public static final int JOB_PRIORITY_LOW = 0;
    public static final int JOB_PRIORITY_MID = 500;
    public static final int JOB_PRIORITY_HIGH = 1000;
    //REQUEST STATE
    public static final int REQUEST_NONE = 0;
    public static final int REQUEST_RUNNING = 10;
    public static final int REQUEST_SUCCEEDED = 11;
    public static final int REQUEST_FAILED = -11;
    //TO HANDLE LOADING PROCESSING BEFORE LAYOUT APPEARS OR ON LAYOUT
    public static final int PROCESSING_BASE_TYPE = 20;
    public static final int PROCESSING_ON_LAYOUT_TYPE = 25;
    //STATUS CODES
    public static final int STATUS_OK = 200;
    public static final int STATUS_ERROR = -200;
    public static final int ERROR_NO_CONNECTION = - 1010;
    public static final int ERROR_SIGNIN_CANCELED = -385;
    public static final int ERROR_SIGNUP_CANCELED = -390;
    private static final int ERROR_EMPTY_REQUIRED_FLDS = -210;
    private static final int ERROR_INVALID_EMAIL = -220;
    private static final int ERROR_EMAIL_EXISTS = -230;
    private static final int ERROR_INVALID_PASSWORD = -240;
    private static final int ERROR_PASSWORDS_MISMATCH = -250;
    private static final int ERROR_INVALID_USERNAME_PASSWORD = -260;
    private static final int ERROR_ACCOUNT_NOT_VERIFIED_YET = -270;
    private static final int ERROR_EMAIL_USED = -280;
    private static final int ERROR_EMAIL_NOT_FOUND = -290;
    private static final int ERROR_NEW_PASSWORD_IS_SAME = -300;
    public static final int ERROR_INVALID_DATE = - 301;
    public static final int ERROR_CITY_NO_MATCH = -302;
    private static final int ERROR_NO_OWNER_EXISTS = -320;

    public static final int GOOGLE_REQ_CODE = 9001;

    public static final String[] cities = {
            "Αθήνα",
            "Θεσσαλονίκη",
            "Πάτρα",
            "Ηράκλειο",
            "Λάρισα",
            "Βόλος",
            "Ιωάννινα",
            "Τρίκαλα",
            "Αγρίνιο",
            "Χαλκίδα",
            "Σέρρες",
            "Αλεξανδρούπολη",
            "Ξάνθη",
            "Κατερίνη",
            "Καλαμάτα",
            "Καβάλα",
            "Χανιά",
            "Λαμία",
            "Κομοτηνή",
            "Ρόδος",
            "Δράμα",
            "Βέροια",
            "Κοζάνη",
            "Καρδίτσα",
            "Ρέθυμνο",
            "Πτολεμαΐδα",
            "Τρίπολη",
            "Κόρινθος",
            "Γέρακας",
            "Γιαννιτσά",
            "Μυτιλήνη",
            "Χίος",
            "Σαλαμίνα",
            "Ελευσίνα",
            "Κέρκυρα",
            "Πύργος",
            "Μέγαρα",
            "Κιλκίς",
            "Θήβα",
            "Άργος",
            "Άρτα",
            "Άρτεμη (Λούτσα)",
            "Λιβαδειά",
            "Ωραιόκαστρο",
            "Αίγιο",
            "Κως",
            "Κορωπί",
            "Πρέβεζα",
            "Νάουσα",
            "Ορεστιάδα",
            "Περαία",
            "Έδεσσα",
            "Φλώρινα",
            "Αμαλιάδα",
            "Παλλήνη",
            "Σπάρτη",
            "Θέρμη",
            "Βάρη",
            "Νέα Μάκρη",
            "Αλεξάνδρεια",
            "Παιανία",
            "Καλύβια Θορικού",
            "Ναύπλιο",
            "Ναύπακτος",
            "Καστοριά",
            "Γρεβενά",
            "Μεσολόγγι",
            "Γάζι",
            "Ιεράπετρα",
            "Κάλυμνος (Πόθια)",
            "Ραφήνα",
            "Λουτράκι",
            "Άγιος Νικόλαος",
            "Ερμούπολη",
            "Ιαλυσός",
            "Μάνδρα",
            "Τύρναβος",
            "Γλυκά Νερά",
            "Άγιος Στέφανος",
            "Διαβατά",
            "Κιάτο",
            "Ανατολή",
            "Ζάκυνθος",
            "Αργοστόλι",
            "Πόρτο Ράφτη",
            "Μαρκόπουλο",
            "Νέα Αρτάκη",
            "Ζεφύρι",
            "Σητεία",
            "Νέα Μουδανιά",
            "Φάρσαλα",
            "Σίνδος",
            "Διδυμότειχο",
            "Σπάτα",
            "Ηγουμενίτσα",
            "Επανομή",
            "Χρυσούπολη",
            "Νέα Μηχανιώνα",
            "Λευκάδα",
            "Νέα Πέραμος",
            "Καλαμπάκα",
            "Σάμος",
            "Αλμυρός",
            "Κουφάλια",
            "Γιάννουλη",
            "Λαγκαδάς",
            "Μουρνιές",
            "Κερατέα",
            "Γαστούνη",
            "Άργος Ορεστικό",
            "Ελασσόνα",
            "Χαλάστρα",
            "Νέα Καλλικράτεια",
            "Τρίλοφος",
            "Καρπενήσι",
            "Μαραθώνας",
            "Λαύριο",
            "Νάξος",
            "Πολύκαστρο",
            "Λιτόχωρο",
            "Άμφισσα",
            "Αίγινα",
            "Νέο Καρλόβασι",
            "Κάτω Αχαΐα",
            "Βασιλικό",
            "Αριδαία",
            "Άνοιξη",
            "Ασβεστοχώρι",
            "Μοίρες",
            "Σούδα",
            "Παραλία Αχαΐας",
            "Κουνουπιδιανά",
            "Οβρυά",
            "Ανάβυσσος",
            "Θρακομακεδόνες",
            "Πολύγυρος",
            "Αμπελώνας",
            "Αφάντου",
            "Μεσσήνη",
            "Νέοι Επιβάτες",
            "Φιλιατρά",
            "Λεοντάρι",
            "Ψαχνά",
            "Μεγαλόπολη",
            "Παλαμάς",
            "Αυλώνας",
            "Μύρινα",
            "Διόνυσος",
            "Σοφάδες",
            "Νεροκούρος",
            "Ξυλόκαστρο",
            "Φίλυρο",
            "Σιάτιστα",
            "Φέρες",
            "Σκύδρα",
            "Πλαγιάρι",
            "Αρχάγγελος",
            "Κρεμαστή",
            "Βροντάδος",
            "Τυμπάκι",
            "Ρίο",
            "Ορχομενός",
            "Κρύα Βρύση",
            "Αγριά",
            "Μακροχώριον",
            "Σιδηρόκαστρο",
            "Νέα Αγχίαλος",
            "Κυπαρισσία",
            "Κάρυστος",
            "Κρυονέρι",
            "Δροσιά",
            "Γαργαλιάνοι"
    };

    public static final String[] races = {
            "Άγνωστο",
            "Αγ. Βερνάρδου - Saint Bernard",
            "Αγγλικό Μπουλντόγκ - English Bulldog",
            "Αγγλικός ποιμενικός - English Sheepdog",
            "Ακίτα - Akita",
            "Αμερικάνικο Σταφορντσάιρ τεριέ - American Staffordshire Terrier",
            "Άσπρος Χάϊλαντς τεριέ - White W.Highlands ter",
            "Αφγανικό λαγωνικό -Afghan Hound",
            "Αφενπίντσερ - Affenpinscher",
            "Βαϊμάρης - Weimaraner",
            "Βελγικός Ποιμενικός - Belgian Sheepdog",
            "Βελγικός Ποιμενικός Μαλινουά - Belgian Malinois",
            "Γαλλικό Μπουλντόγκ - French Bulldog",
            "Γερμανικός ποιμενικός Αλσατίας",
            "Γιόρκσάϊρ τεριέ - Yorkshire Terrier",
            "Γκόλντεν Ριτρίβερ - Golden Retriever",
            "Γκρέϊ χάουντ - Grey Hound",
            "Δαλματίας - Dalmatian",
            "Ιαπωνέζικο Τσίν - Japanese Chin",
            "Κανέ Κόρσο - Cane Corso",
            "Κανίς Βερικοκί - Caniche apricot",
            "Κανίς Λευκό - Canishe White",
            "Κανίς μίνι μαύρο - Canishe black",
            "Κερν τεριέ - Cairn terrier",
            "Κόκερ Σπάνιελ - Cocker spaniel",
            "Κόκερ Σπάνιελ κλάμπερ - Cocker Spaniel Clumber",
            "Κόλει - Collie",
            "Λαμπραντόρ ριτρίβερ - Labrador retriever",
            "Λάσα Άπσο - Lhasa Apso",
            "Μαλαμούτ Αλάσκας - Malamut",
            "Μαλτέζ - Maltese",
            "Μαστίφ Ναπολιτάνο - Mastiff Napolitano",
            "Μεγάλος Δανίας - Great Dane",
            "Μπασέντζι - Basenji",
            "Μπάσετ χάουντ - Basset Hound",
            "Μπίγκλ - Beagle",
            "Μπισόν Φρίζ - Bichon Frise",
            "Μπλαντχάουντ - Bloodhound",
            "Μπόξερ - Boxer",
            "Μπορζόϊ - Borzoi",
            "Μπουβιέ Φλάνδρας - Bouvier de Flandres",
            "Μπούλ Μαστίφ - Bull mastif",
            "Μπουλ τεριέ - Bull terrier",
            "Νέας Γης - New found land",
            "Ντάντι Ντίνμοντ - Dandi Dinmont terrier",
            "Ντάτς χάουντ - Dach hound",
            "Ντογκ ντε Μπορντώ - Dogue de Bordeaux",
            "Ντόγκο Αρτζεντίνο - Dogo Argentino",
            "Ντόμπερμαν - Doberman",
            "Ορεινός πυρηναίων - Great Pyrenees",
            "Πάγκ - Pug",
            "Παπιγιόν - Papijon",
            "Πεκινουά - Pekignese",
            "Πίντσερ - Pinscher",
            "Πίτ μπούλ - pitbull",
            "Ποιμενικός Καυκάσου - Caucasian Sheepdog",
            "Πόϊντερ - Pointer",
            "Πομεράνιαν - Pomeranian",
            "Ροδέσιαν Ρίτζμπακ - Rhodesian Ridgeback",
            "Ροτβάϊλερ - Rottweiler",
            "Σαμογιέντ - Samoyede",
            "Σαρ Πέι - Shar pei",
            "Σέτερ Ιρλανδικός - Setter Irish",
            "Σέτερ Λάβερακ - Setter laverack",
            "Σέτλαντ ποιμενικός - Shetland Sheepdog",
            "Σέττερ Γκόρντον - Setter Gordon",
            "Σιβηριανός Χάσκι - Siberian Husky",
            "Σιχ Τσού - Shih Tzu",
            "Σνάουζερ - Schnauzer",
            "Σπάνιελ Βασιλιά Καρόλου Καφέ - King Charles Spaniel",
            "Σπίτς - spitz",
            "Τεριέ Αϊρντέιλ - Airedale terrier",
            "Τεριέ Βοστόνης - Boston terrier",
            "Τζακ Ράσελ Τεριέ - Jack Russell Terrier",
            "Τσιχουάουα - Chihuahua",
            "Τσόου Τσόου - Chow chow",
            "Φίλα Μπραζιλέιρο - Fila Brasileiro",
            "Φοξ τεριέ - Fox terrier",
            "Χουϊπετ - Whippet"
    };

    private static final SparseIntArray inputValidationCodes = new SparseIntArray();
    private static final SparseArray<Throwable> errorThrowables = new SparseArray<>();

    public static SparseIntArray getCodes() {
        inputValidationCodes.put(ERROR_NO_CONNECTION, R.string.no_internet_connection);
        inputValidationCodes.put(STATUS_ERROR, R.string.error);
        inputValidationCodes.put(ERROR_SIGNIN_CANCELED, R.string.signin_cancel);
        inputValidationCodes.put(ERROR_SIGNUP_CANCELED, R.string.sign_up_cancel);
        inputValidationCodes.put(ERROR_EMPTY_REQUIRED_FLDS, R.string.empty_required_fields);
        inputValidationCodes.put(ERROR_INVALID_EMAIL, R.string.invalid_email);
        inputValidationCodes.put(ERROR_EMAIL_EXISTS, R.string.email_not_available);
        inputValidationCodes.put(ERROR_INVALID_PASSWORD, R.string.invalid_password);
        inputValidationCodes.put(ERROR_PASSWORDS_MISMATCH, R.string.pass_mismatch);
        inputValidationCodes.put(ERROR_INVALID_USERNAME_PASSWORD, R.string.invalid_email_password);
        inputValidationCodes.put(ERROR_ACCOUNT_NOT_VERIFIED_YET, R.string.account_not_verified);
        inputValidationCodes.put(ERROR_EMAIL_USED, R.string.email_in_use);
        inputValidationCodes.put(ERROR_EMAIL_NOT_FOUND, R.string.email_not_found);
        inputValidationCodes.put(ERROR_NEW_PASSWORD_IS_SAME, R.string.new_password_is_same);
        inputValidationCodes.put(ERROR_INVALID_DATE, R.string.invalid_date);
        inputValidationCodes.put(ERROR_CITY_NO_MATCH, R.string.city_no_match);
        inputValidationCodes.put(ERROR_NO_OWNER_EXISTS, R.string.no_owner_exists);
//        inputValidationCodes.put(ERROR_INVALID_IMAGE_SIZE, R.string.invalid_image_size);
//        inputValidationCodes.put(ERROR_NO_OWNER_EXISTS, R.string.no_owner_exists);
//        inputValidationCodes.put(ERROR_AGE_NOT_VALID, R.string.not_valid_age);
//        inputValidationCodes.put(ERROR_BOOKING_ALREADY_PENDING, R.string.booking_already_pending);
//        inputValidationCodes.put(ERROR_NO_PROF_EXISTS, R.string.no_prof_exists);

        return inputValidationCodes;
    }

    public static SparseArray<Throwable> getErrorThrowables() {
        errorThrowables.put(STATUS_ERROR, new StatusErrorException());
        errorThrowables.put(ERROR_NO_OWNER_EXISTS, new NoOnwerExistsException());
        return errorThrowables;
    }
}
