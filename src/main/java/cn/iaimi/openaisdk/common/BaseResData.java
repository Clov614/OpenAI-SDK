package cn.iaimi.openaisdk.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Clov614
 * @version 1.0
 * DATE 2024/4/6
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseResData<D, U> {

    private D data;

    private U usage;
}
