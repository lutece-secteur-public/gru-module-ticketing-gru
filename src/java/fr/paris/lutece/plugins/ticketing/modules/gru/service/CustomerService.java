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
package fr.paris.lutece.plugins.ticketing.modules.gru.service;

import fr.paris.lutece.plugins.gru.business.customer.Customer;
import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 * CustomerService
 */
public final class CustomerService
{
    private static final String BEAN_CUSTOMER_INFO_SERVICE = "ticketing-gru.customerinfoService";
    private static ICustomerInfoService _customerInfoService;
    private static CustomerService _singleton;

    /** private constructor */
    private CustomerService(  )
    {
    }

    /**
     * Return the unique instance
     * @return The instance
     */
    public static CustomerService instance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new CustomerService(  );
            _customerInfoService = SpringContextService.getBean( BEAN_CUSTOMER_INFO_SERVICE );
        }

        return _singleton;
    }

    /**
     * Retrieve a customer by its guid
     * @param strGuid The GUID
     * @return The customer
     */
    public Customer getCustomerByGuid( String strGuid )
    {
        return _customerInfoService.getCustomerByGuid( strGuid );
    }

    /**
     * Retrieve the customer by its ID
     * @param strCid The customer ID
     * @return The customer
     */
    public Customer getCustomerByCid( String strCid )
    {
        return _customerInfoService.getCustomerByCid( strCid );
    }

    /**
     * Create a new customer
     * @param customer The customer
     * @return  The created customer
     */
    public Customer createCustomer( Customer customer )
    {
        return _customerInfoService.createCustomer( customer );
    }
}
