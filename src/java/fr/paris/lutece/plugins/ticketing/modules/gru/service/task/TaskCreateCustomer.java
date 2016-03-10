/*
 * Copyright (c) 2002-2015, Mairie de Paris
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
package fr.paris.lutece.plugins.ticketing.modules.gru.service.task;

import fr.paris.lutece.plugins.gru.business.customer.Customer;
import fr.paris.lutece.plugins.ticketing.business.Ticket;
import fr.paris.lutece.plugins.ticketing.business.TicketHome;
import fr.paris.lutece.plugins.ticketing.modules.gru.business.dto.UserDTO;
import fr.paris.lutece.plugins.ticketing.modules.gru.service.CustomerService;
import fr.paris.lutece.plugins.ticketing.modules.gru.service.UserInfoService;
import fr.paris.lutece.plugins.workflow.modules.ticketing.service.task.AbstractTicketingTask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents a task to create a customer
 */
public class TaskCreateCustomer extends AbstractTicketingTask
{
    private static final String MESSAGE_CREATE_CUSTOMER = "module.ticketing.gru.task_create_customer.info";
    private static final String MESSAGE_UNKNOWN_EXTRA_ATTRIBUTES = "module.ticketing.gru.task_create_customer.unknownExtraAttributes";
    private static final String MESSAGE_UNKNOWN_ID = "module.ticketing.gru.task_create_customer.unknownId";
    private static final String MESSAGE_CREATE_CUSTOMER_TASK = "module.ticketing.gru.task_create_customer.title";

    /**
     * Methode which create a gru Customer
     * @param user User from SSO database
     * @param strUserId ID from Flux
     * @return the Customer
     */
    private static Customer buildCustomer( UserDTO user, String strUserId )
    {
        Customer gruCustomer = new fr.paris.lutece.plugins.gru.business.customer.Customer(  );
        gruCustomer.setFirstname( setEmptyValueWhenNullValue( user.getFirstname(  ) ) );
        gruCustomer.setLastname( setEmptyValueWhenNullValue( user.getLastname(  ) ) );
        gruCustomer.setEmail( setEmptyValueWhenNullValue( user.getEmail(  ) ) );
        gruCustomer.setAccountGuid( setEmptyValueWhenNullValue( strUserId ) );
        gruCustomer.setAccountLogin( setEmptyValueWhenNullValue( user.getEmail(  ) ) );
        gruCustomer.setMobilePhone( setEmptyValueWhenNullValue( user.getTelephoneNumber(  ) ) );
        gruCustomer.setExtrasAttributes( I18nService.getLocalizedString( MESSAGE_UNKNOWN_EXTRA_ATTRIBUTES, Locale.FRENCH ) );

        return gruCustomer;
    }

    /**
     * return a userDTO from ticket value
     * @param ticket ticket used to initialise DTO
     * @return userDto initialized wit ticket infos
     */
    private static UserDTO buildUserFromTicket( Ticket ticket )
    {
        UserDTO user = null;

        if ( ticket != null )
        {
            user = new UserDTO(  );
            user.setFirstname( ticket.getFirstname(  ) );
            user.setLastname( ticket.getLastname(  ) );
            user.setEmail( ticket.getEmail(  ) );
            user.setUid( ticket.getGuid(  ) );
            user.setCivility( ticket.getUserTitle(  ) );
            user.setTelephoneNumber( ticket.getMobilePhoneNumber(  ) );
        }

        return user;
    }

    /**
     * retruns empty string if value is null
     * @param value value to test
     * @return empty string if value is null
     */
    private static String setEmptyValueWhenNullValue( String value )
    {
        return ( StringUtils.isEmpty( value ) ) ? "" : value;
    }

    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_CREATE_CUSTOMER_TASK, locale );
    }

    @Override
    protected String processTicketingTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        Ticket ticket = getTicket( nIdResourceHistory );

        fr.paris.lutece.plugins.gru.business.customer.Customer gruCustomer = null;
        UserDTO userDto = null;

        String strCidFromTicket = ticket.getCustomerId(  );
        String strGuidFromTicket = ticket.getGuid(  );

        // CASE no cid
        if ( StringUtils.isEmpty( strCidFromTicket ) )
        {
            if ( !StringUtils.isEmpty( strGuidFromTicket ) )
            {
                //if guid is provided => we try to retrieve linked customer
                gruCustomer = CustomerService.instance(  ).getCustomerByGuid( ticket.getGuid(  ) );
                userDto = UserInfoService.instance(  ).getUserInfo( strGuidFromTicket );
            }

            if ( gruCustomer == null )
            {
                //customer is unknown / not found => we create it
                if ( userDto == null )
                {
                    userDto = buildUserFromTicket( ticket );
                }

                //create customer
                gruCustomer = CustomerService.instance(  ).createCustomer( buildCustomer( userDto, strGuidFromTicket ) );
                AppLogService.info( "New user created the guid : <" + strGuidFromTicket + "> its customer id is : <" +
                    gruCustomer.getId(  ) + ">" );
            }

            //update CID
            ticket.setCustomerId( String.valueOf( gruCustomer.getId(  ) ) );
            TicketHome.update( ticket );
        }
        else
        {
            if ( StringUtils.isEmpty( strGuidFromTicket ) )
            {
                if ( StringUtils.isNumeric( strCidFromTicket ) )
                {
                    // CASE : cid but no guid:  find customer info in GRU database => try to retrieve guid from customer
                    gruCustomer = CustomerService.instance(  ).getCustomerByCid( strCidFromTicket );
                }
                else
                {
                    AppLogService.error( "Provided customerId is not numeric: <" + strCidFromTicket + ">" );
                }

                if ( gruCustomer != null )
                {
                    ticket.setGuid( gruCustomer.getAccountGuid(  ) );
                    TicketHome.update( ticket );
                }
                else
                {
                    AppLogService.info( "No guid found for user cid : <" + strCidFromTicket + ">" );
                }
            }
        }

        return MessageFormat.format( I18nService.getLocalizedString( MESSAGE_CREATE_CUSTOMER, Locale.FRENCH ),
            ( StringUtils.isNotEmpty( ticket.getCustomerId(  ) ) ) ? String.valueOf( ticket.getCustomerId(  ) )
                                                                   : I18nService.getLocalizedString( 
                MESSAGE_UNKNOWN_ID, Locale.FRENCH ),
            ( StringUtils.isNotEmpty( ticket.getGuid(  ) ) ) ? ticket.getGuid(  )
                                                             : I18nService.getLocalizedString( MESSAGE_UNKNOWN_ID,
                Locale.FRENCH ) );
    }
}
