package srt.operator;

import cn.hutool.core.lang.Console;
import srt.operator.imp.SrtNodeSearch;
import srt.operator.imp.SrtTimeShift;

import javax.management.JMException;
import java.util.HashMap;

public class SrtOperatorFactory {
    public SrtOperatorFactory() {
    }

    public static SrtOperator getSrtOperator(String name, HashMap parameters) throws JMException {
        if (name.equalsIgnoreCase("SrtTimeShift")) {
            return new SrtTimeShift(parameters);
        } else if (name.equalsIgnoreCase("SrtNodeSearch")) {
            return new SrtNodeSearch(parameters);
        } else {
            Console.log("SrtOperatorFactory.getSrtOperator. Operator " + name + " not found ");
            throw new JMException("Exception in " + name + ".getSrtOperator()");
        }
    }
}
