package bean;

/**
 * Created by User on 13.08.2017.
 */

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "document")
public class DocumentEncashment {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String number;
    @DatabaseField(columnName = "dateDoc", dataType = DataType.DATE_STRING,
            format = "yyyy-MM-dd")
    private Date dateDoc;
    @DatabaseField
    private float summ;
    @DatabaseField
    private String contractorName;
    @DatabaseField
    private String contractorCode;
    @DatabaseField
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDateDoc() {
        return dateDoc;
    }

    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public float getSumm() {
        return summ;
    }

    public void setSumm(float summ) {
        this.summ = summ;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(String contractorCode) {
        this.contractorCode = contractorCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
