/*
 * Copyright (c) 2002-2013, Mairie de Paris
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.ticketing.modules.gru.business.dto;


/**
 * This is the business class for the object OpenAMUserDTO
 */
public class UserDTO
{
    // Variables declarations 
    private String _strUid;
    private String _strCivility;
    private String _strFirstname;
    private String _strLastname;
    private String _strTelephoneNumber;
    private String _strBirthday;
    private String _strStreet;
    private String _strPostalCode;
    private String _strCity;
    private String _strCityOfBirth;
    private String _strEmail;
    private String _strStayConnected;

    /**
    * Returns the Uid
    * @return The Uid
    */
    public String getUid(  )
    {
        return _strUid;
    }

    /**
     * Sets the Uid
     * @param strUid The Uid
     */
    public void setUid( String strUid )
    {
        _strUid = strUid;
    }

    /**
     * Returns the Civility
     * @return The Civility
     */
    public String getCivility(  )
    {
        return _strCivility;
    }

    /**
     * Sets the Civility
     * @param strCivility The Civility
     */
    public void setCivility( String strCivility )
    {
        _strCivility = strCivility;
    }

    /**
     * Returns the Firstname
     * @return The Firstname
     */
    public String getFirstname(  )
    {
        return _strFirstname;
    }

    /**
     * Sets the Firstname
     * @param strFirstname The Firstname
     */
    public void setFirstname( String strFirstname )
    {
        _strFirstname = strFirstname;
    }

    /**
     * Returns the Lastname
     * @return The Lastname
     */
    public String getLastname(  )
    {
        return _strLastname;
    }

    /**
     * Sets the Lastname
     * @param strLastname The Lastname
     */
    public void setLastname( String strLastname )
    {
        _strLastname = strLastname;
    }

    /**
     * Returns the TelephoneNumber
     * @return The TelephoneNumber
     */
    public String getTelephoneNumber(  )
    {
        return _strTelephoneNumber;
    }

    /**
     * Sets the TelephoneNumber
     * @param strTelephoneNumber The TelephoneNumber
     */
    public void setTelephoneNumber( String strTelephoneNumber )
    {
        _strTelephoneNumber = strTelephoneNumber;
    }

    /**
     * Returns the Birthday
     * @return The Birthday
     */
    public String getBirthday(  )
    {
        return _strBirthday;
    }

    /**
     * Sets the Birthday
     * @param strBirthday The Birthday
     */
    public void setBirthday( String strBirthday )
    {
        _strBirthday = strBirthday;
    }

    /**
     * Returns the Street
     * @return The Street
     */
    public String getStreet(  )
    {
        return _strStreet;
    }

    /**
     * Sets the Street
     * @param strStreet The Street
     */
    public void setStreet( String strStreet )
    {
        _strStreet = strStreet;
    }

    /**
     * Returns the PostalCode
     * @return The PostalCode
     */
    public String getPostalCode(  )
    {
        return _strPostalCode;
    }

    /**
     * Sets the PostalCode
     * @param strPostalCode The PostalCode
     */
    public void setPostalCode( String strPostalCode )
    {
        _strPostalCode = strPostalCode;
    }

    /**
     * Returns the City
     * @return The City
     */
    public String getCity(  )
    {
        return _strCity;
    }

    /**
     * Sets the City
     * @param strCity The City
     */
    public void setCity( String strCity )
    {
        _strCity = strCity;
    }

    /**
     * Returns the CityOfBirth
     * @return The CityOfBirth
     */
    public String getCityOfBirth(  )
    {
        return _strCityOfBirth;
    }

    /**
     * Sets the CityOfBirth
     * @param strCityOfBirth The CityOfBirth
     */
    public void setCityOfBirth( String strCityOfBirth )
    {
        _strCityOfBirth = strCityOfBirth;
    }

    /**
     * returns email
     * @return email
     */
    public String getEmail(  )
    {
        return _strEmail;
    }

    /**
     * set email
     * @param strEmail email to set
     */
    public void setEmail( String strEmail )
    {
        this._strEmail = strEmail;
    }

    /**
    * Returns the StayConnected
    * @return The StayConnected
    */
    public String getStayConnected(  )
    {
        return _strStayConnected;
    }

    /**
     * Sets the StayConnected
     * @param strStayConnected The StayConnected
     */
    public void setStayConnected( String strStayConnected )
    {
        _strStayConnected = strStayConnected;
    }
}
