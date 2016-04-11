package works.tonny.mobile.widget;

import java.util.LinkedHashMap;

/**
 * Created by tonny on 2015/8/1.
 */
public class IDLinkedHashMap extends LinkedHashMap<String, Object> {

    private String idField;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (idField == null) {
            return super.equals(o);
        }
        IDLinkedHashMap idHashMap = (IDLinkedHashMap) o;
        return idField.equals(idHashMap.idField) && get(idField).equals(idHashMap.get(idField));
    }

    @Override
    public int hashCode() {
        if (idField == null)
            return super.hashCode();
        else
            return get(idField).hashCode();
    }


    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }
}
