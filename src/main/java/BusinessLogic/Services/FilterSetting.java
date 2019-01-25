package BusinessLogic.Services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;

@Getter
@Setter
@NoArgsConstructor
public class FilterSetting {
    private String filterName;
    private LinkedList<String> filterValues;

    public void addValue(String newValue){
        filterValues.add(newValue);
    }
}
