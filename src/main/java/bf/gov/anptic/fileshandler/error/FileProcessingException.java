package bf.gov.anptic.fileshandler.error;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;

import java.net.URI;

public class FileProcessingException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;

    public FileProcessingException(URI uri, String title, StatusType statusType) {
        super(uri, title, statusType);
    }
}
