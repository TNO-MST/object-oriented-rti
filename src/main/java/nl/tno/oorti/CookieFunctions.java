package nl.tno.oorti;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper methods for getting attribute or parameter objects given a cookie object. The cookie
 * object is the object provided on the class publication or subscription.
 *
 * @author Tom van den Berg (TNO, The Netherlands)
 */
public class CookieFunctions {

  public static Set<OOattribute> getAttributeSet(
      Set<OOattribute> theAttributes, Object... cookies) {
    Set<OOattribute> result = new HashSet<>();
    for (Object cookie : cookies) {
      for (OOattribute attribute : theAttributes) {
        if (attribute.getCookie() != null) {
          if (attribute.getCookie().equals(cookie)) {
            result.add(attribute);
            break;
          }
        }
      }
    }
    return result;
  }

  public static OOattribute getAttribute(Set<OOattribute> theAttributes, Object cookie) {
    for (OOattribute attribute : theAttributes) {
      if (attribute.getCookie() != null) {
        if (attribute.getCookie().equals(cookie)) {
          return attribute;
        }
      }
    }
    return null;
  }

  public static Set<OOparameter> getParameterSet(
      Set<OOparameter> theParameters, Object... cookies) {
    Set<OOparameter> result = new HashSet<>();
    for (Object cookie : cookies) {
      for (OOparameter parameter : theParameters) {
        if (parameter.getCookie() != null) {
          if (parameter.getCookie().equals(cookie)) {
            result.add(parameter);
            break;
          }
        }
      }
    }
    return result;
  }

  public static OOparameter getParameter(Set<OOparameter> theParameters, Object cookie) {
    for (OOparameter parameter : theParameters) {
      if (parameter.getCookie() != null) {
        if (parameter.getCookie().equals(cookie)) {
          return parameter;
        }
      }
    }
    return null;
  }

  public static boolean containsAttribute(Set<OOattribute> theAttributes, Object cookie) {
    return getAttribute(theAttributes, cookie) != null;
  }

  public static boolean containsParameter(Set<OOparameter> theParameters, Object cookie) {
    return getParameter(theParameters, cookie) != null;
  }
}
