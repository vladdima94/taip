/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryProtocolTests;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import QueryProtocol.QueryProtocol;
import junit.framework.TestCase;
import static junit.framework.TestCase.fail;

/**
 *
 * @author Vlad
 */
public class QueryProtocolTests extends TestCase{
    private QueryProtocol test;
    
    public QueryProtocolTests() {
        test = new QueryProtocol();
    }

    public void testGenerateEntityToken()
    {
        String token = test.generateEntityToken();
        if(token == null)
        {
            fail("No token was generated!");
        }
        else if(token.length() < 30)
        {
            fail("Empty or too small");
        }
    }
}

