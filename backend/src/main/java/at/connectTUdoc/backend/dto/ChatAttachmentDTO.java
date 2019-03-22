package at.connectTUdoc.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class ChatAttachmentDTO extends AbstractDTO {
    @JsonIgnore
    private byte[] fileContent;
    private String fileName;
    private String fileType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatAttachmentDTO that = (ChatAttachmentDTO) o;
        return fileName.equals(that.fileName) &&
                fileType.equals(that.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash( fileName, fileType);
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void validate() {
        if(fileContent == null || fileContent.length == 0) {
            throw new IllegalStateException("Empty file attached");
        }
        if(StringUtils.isEmpty(fileName)) {
            throw new IllegalStateException("File name was empty");
        }
    }
}
