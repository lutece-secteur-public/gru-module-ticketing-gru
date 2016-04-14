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

import fr.paris.lutece.plugins.customerprovisioning.business.UserDTO;
import fr.paris.lutece.plugins.customerprovisioning.services.ProvisioningService;
import fr.paris.lutece.plugins.gru.business.customer.Customer;
import fr.paris.lutece.plugins.ticketing.business.ticket.Ticket;
import fr.paris.lutece.plugins.ticketing.business.ticket.TicketHome;
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
    private static final String MESSAGE_UNKNOWN_ID = "module.ticketing.gru.task_create_customer.unknownId";
    private static final String MESSAGE_CREATE_CUSTOMER_TASK = "module.ticketing.gru.task_create_customer.title";
    private static final String STRING_NULL = "NULL";

    
    
    
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
            user.setFixedPhoneNumber( ticket.getFixedPhoneNumber( ) );
            user.setTelephoneNumber( ticket.getMobilePhoneNumber(  ) );   
        }

        return user;
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

        UserDTO userDto = buildUserFromTicket( ticket );
        boolean bMustBeUpdated = false;

        String strCidFromTicket = ticket.getCustomerId(  );
        String strGuidFromTicket = ticket.getGuid(  );

        Customer gruCustomer = ProvisioningService.processGuidCuid( strGuidFromTicket, strCidFromTicket, userDto );

        if ( ( gruCustomer != null ) && !gruCustomer.getAccountGuid(  ).equals( STRING_NULL ) &&
                !gruCustomer.getAccountGuid(  ).equals( ticket.getGuid(  ) ) )
        {
            //guid changed
            ticket.setGuid( gruCustomer.getAccountGuid(  ) );
            bMustBeUpdated = true;
        }

        if ( ( gruCustomer != null ) &&
                ( ( ticket.getCustomerId(  ) == null ) ||
                ( ticket.getCustomerId(  ) != String.valueOf( gruCustomer.getId(  ) ) ) ) )
        {
            //cid changed
            ticket.setCustomerId( String.valueOf( gruCustomer.getId(  ) ) );
            bMustBeUpdated = true;
        }

        if ( bMustBeUpdated )
        {
            TicketHome.update( ticket );
        }

        AppLogService.info( MessageFormat.format( I18nService.getLocalizedString( MESSAGE_CREATE_CUSTOMER, Locale.FRENCH ),
                ( StringUtils.isNotEmpty( ticket.getCustomerId(  ) ) ) ? String.valueOf( ticket.getCustomerId(  ) )
                                                                       : I18nService.getLocalizedString( 
                    MESSAGE_UNKNOWN_ID, Locale.FRENCH ),
                ( StringUtils.isNotEmpty( ticket.getGuid(  ) ) ) ? ticket.getGuid(  )
                                                                 : I18nService.getLocalizedString( MESSAGE_UNKNOWN_ID,
                    Locale.FRENCH ) ) );

        return null;
    }
}
