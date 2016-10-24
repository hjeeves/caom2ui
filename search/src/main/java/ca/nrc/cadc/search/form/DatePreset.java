/*
 ************************************************************************
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 *
 * (c) 2015.                         (c) 2015.
 * National Research Council            Conseil national de recherches
 * Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
 * All rights reserved                  Tous droits reserves
 *
 * NRC disclaims any warranties         Le CNRC denie toute garantie
 * expressed, implied, or statu-        enoncee, implicite ou legale,
 * tory, of any kind with respect       de quelque nature que se soit,
 * to the software, including           concernant le logiciel, y com-
 * without limitation any war-          pris sans restriction toute
 * ranty of merchantability or          garantie de valeur marchande
 * fitness for a particular pur-        ou de pertinence pour un usage
 * pose.  NRC shall not be liable       particulier.  Le CNRC ne
 * in any event for any damages,        pourra en aucun cas etre tenu
 * whether direct or indirect,          responsable de tout dommage,
 * special or general, consequen-       direct ou indirect, particul-
 * tial or incidental, arising          ier ou general, accessoire ou
 * from the use of the software.        fortuit, resultant de l'utili-
 *                                      sation du logiciel.
 *
 *
 * @author jenkinsd
 * 1/6/15 - 2:47 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.search.form;


import java.util.Calendar;

import ca.nrc.cadc.date.DateUtil;


public enum DatePreset
{
    PAST_24_HOURS, PAST_WEEK, PAST_MONTH;

    static final String PATTERN = DateUtil.ISO_DATE_FORMAT;
    
    
    String getStringValue(final java.util.Date d)
    {
        final String stringVal;

        switch(this)
        {
            case PAST_24_HOURS:
            {
                final Calendar lowerCal = Calendar.getInstance(DateUtil.UTC);
                lowerCal.setTime(d);
                lowerCal.add(Calendar.DAY_OF_MONTH, -1);

                stringVal = DateUtil.getDateFormat(PATTERN,
                                                   DateUtil.UTC)
                                    .format(lowerCal.getTime()) + ".."
                            + DateUtil.getDateFormat(PATTERN, DateUtil.UTC)
                                    .format(d);
                break;
            }

            case PAST_WEEK:
            {
                final Calendar lowerCal = Calendar.getInstance(DateUtil.UTC);
                lowerCal.setTime(d);
                lowerCal.add(Calendar.DAY_OF_MONTH, -7);

                stringVal = DateUtil.getDateFormat(PATTERN,
                                                   DateUtil.UTC)
                                    .format(lowerCal.getTime()) + ".."
                            + DateUtil.getDateFormat(PATTERN, DateUtil.UTC)
                                    .format(d);
                break;
            }

            case PAST_MONTH:
            {
                final Calendar lowerCal = Calendar.getInstance(DateUtil.UTC);
                lowerCal.setTime(d);
                lowerCal.add(Calendar.MONTH, -1);

                stringVal = DateUtil.getDateFormat(PATTERN,
                                                   DateUtil.UTC)
                                    .format(lowerCal.getTime()) + ".."
                            + DateUtil.getDateFormat(PATTERN, DateUtil.UTC)
                                    .format(d);
                break;
            }

            default:
            {
                throw new IllegalArgumentException(
                        "Unsupported or unknown preset: " + name());
            }
        }

        return stringVal;
    }
}
