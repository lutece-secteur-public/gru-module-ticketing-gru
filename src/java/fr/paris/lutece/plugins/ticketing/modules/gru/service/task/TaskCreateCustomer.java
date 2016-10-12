/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

import fr.paris.lutece.plugins.identitystore.web.rs.dto.AttributeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.AuthorDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityChangeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.ticketing.business.ticket.Ticket;
import fr.paris.lutece.plugins.ticketing.business.ticket.TicketHome;
import fr.paris.lutece.plugins.ticketing.service.identity.TicketingIdentityService;
import fr.paris.lutece.plugins.ticketing.web.TicketingConstants;
import fr.paris.lutece.plugins.workflow.modules.ticketing.service.task.AbstractTicketingTask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents a task to create a customer
 */
public class TaskCreateCustomer extends AbstractTicketingTask
{
    // Messages
    private static final String MESSAGE_CREATE_CUSTOMER = "module.ticketing.gru.task_create_customer.info";
    private static final String MESSAGE_UNKNOWN_ID = "module.ticketing.gru.task_create_customer.unknownId";
    private static final String MESSAGE_CREATE_CUSTOMER_TASK = "module.ticketing.gru.task_create_customer.title";

    /**
     * Builds a {@link IdentityDto} from the specified ticket
     *
     * @param ticket
     *            ticket used to initialize the identity
     * @return the identity
     */
    private static IdentityDto buildIdentity( Ticket ticket )
    {
        IdentityDto identityDto = new IdentityDto(  );
        Map<String, AttributeDto> mapAttributes = new HashMap<String, AttributeDto>(  );

        if ( ticket != null )
        {
            identityDto.setConnectionId( ticket.getGuid(  ) );

            if ( !StringUtils.isEmpty( ticket.getCustomerId(  ) ) )
            {
                identityDto.setCustomerId( Integer.parseInt( ticket.getCustomerId(  ) ) );
            }

            identityDto.setAttributes( mapAttributes );

            try
            {
                String strIdUserTitle = Integer.toString( ticket.getIdUserTitle(  ) );
                setAttribute( identityDto, TicketingConstants.ATTRIBUTE_IDENTITY_GENDER, strIdUserTitle );
            }
            catch ( NumberFormatException e )
            {
                // The attribute gender is not provided
            }

            setAttribute( identityDto, TicketingConstants.ATTRIBUTE_IDENTITY_NAME_GIVEN, ticket.getFirstname(  ) );
            setAttribute( identityDto, TicketingConstants.ATTRIBUTE_IDENTITY_NAME_PREFERRED_NAME, ticket.getLastname(  ) );
            setAttribute( identityDto, TicketingConstants.ATTRIBUTE_IDENTITY_HOMEINFO_ONLINE_EMAIL, ticket.getEmail(  ) );
            setAttribute( identityDto, TicketingConstants.ATTRIBUTE_IDENTITY_HOMEINFO_TELECOM_TELEPHONE_NUMBER,
                ticket.getFixedPhoneNumber(  ) );
            setAttribute( identityDto, TicketingConstants.ATTRIBUTE_IDENTITY_HOMEINFO_TELECOM_MOBILE_NUMBER,
                ticket.getMobilePhoneNumber(  ) );
        }

        return identityDto;
    }

    /**
     * Sets an attribute into the specified identity
     * @param identityDto the identity
     * @param strCode the attribute code
     * @param strValue the attribute value
     */
    private static void setAttribute( IdentityDto identityDto, String strCode, String strValue )
    {
        AttributeDto attributeDto = new AttributeDto(  );
        attributeDto.setKey( strCode );
        attributeDto.setValue( strValue );

        identityDto.getAttributes(  ).put( attributeDto.getKey(  ), attributeDto );
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

        boolean bMustBeUpdated = false;

        IdentityChangeDto identityChangeDto = new IdentityChangeDto(  );
        IdentityDto identityDto = buildIdentity( ticket );

        identityChangeDto.setIdentity( identityDto );

        AuthorDto authorDto = new AuthorDto(  );
        authorDto.setApplicationCode( TicketingConstants.APPLICATION_CODE );

        identityChangeDto.setAuthor( authorDto );

        identityDto = TicketingIdentityService.getInstance(  ).getIdentityService(  ).createIdentity( identityChangeDto );

        String strUpdatedGuid = identityDto.getConnectionId(  );

        if ( ( strUpdatedGuid != null ) && !strUpdatedGuid.equals( ticket.getGuid(  ) ) )
        {
            // guid changed
            ticket.setGuid( strUpdatedGuid );
            bMustBeUpdated = true;
        }

        String strUpdatedCid = String.valueOf( identityDto.getCustomerId(  ) );

        if ( !strUpdatedCid.equals( ticket.getCustomerId(  ) ) )
        {
            // cid changed
            ticket.setCustomerId( strUpdatedCid );
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
