import com.kttdevelopment.simplehttpserver.ContextUtil;
import org.junit.Assert;
import org.junit.Test;

public class ContextUtilTests {

    @Test
    public void testContexts(){
        Assert.assertEquals("Failed test on blank (leading slash)"  ,"/", ContextUtil.getContext("",true,false));
        Assert.assertEquals("Failed test on blank (trailing slash)" ,"/", ContextUtil.getContext("",false,true));
        Assert.assertEquals("Failed test on blank (both slashes)"   ,"/", ContextUtil.getContext("",true,true));

        Assert.assertEquals("Failed test on single (leading slash)"  ,"/a" , ContextUtil.getContext("a",true,false));
        Assert.assertEquals("Failed test on single (trailing slash)" ,"a/" , ContextUtil.getContext("a",false,true));
        Assert.assertEquals("Failed test on single (both slashes)"   ,"/a/", ContextUtil.getContext("a",true,true));

        Assert.assertEquals("/testLeading"  , ContextUtil.getContext("testLeading",true,false));
        Assert.assertEquals("testTrailing/" , ContextUtil.getContext("testTrailing",false,true));
        Assert.assertEquals("testNone"      , ContextUtil.getContext("/testNone/",false,false));
        Assert.assertEquals("/testBoth/"    , ContextUtil.getContext("testBoth",true,true));

        Assert.assertEquals("testBackSlash/"    , ContextUtil.getContext("testBackSlash\\",false,true));
        Assert.assertEquals("testConsBackSlash/", ContextUtil.getContext("testConsBackSlash\\\\",false,true));
        Assert.assertEquals("testConsFwdSlash/" , ContextUtil.getContext("testConsFwdSlash//",false,true));
    }

    @Test
    public void testJoin(){
        Assert.assertEquals("Failed test on last blank join" ,"a"  ,ContextUtil.joinContexts(false,false,"a",""));
        Assert.assertEquals("Failed test on last blank join+","/a/",ContextUtil.joinContexts(true,true,"a",""));

        Assert.assertEquals("Failed test on first blank join" ,"a"  ,ContextUtil.joinContexts(false,false,"","a"));
        Assert.assertEquals("Failed test on first blank join+","/a/",ContextUtil.joinContexts(true,true,"","a"));

        Assert.assertEquals("Failed test on both blank join" ,""  ,ContextUtil.joinContexts(false,false,"",""));
        Assert.assertEquals("Failed test on both blank join+","/",ContextUtil.joinContexts(true,true,"",""));

        Assert.assertEquals("trailing/slash"    ,ContextUtil.joinContexts(false,false,"trailing/","slash/"));
        Assert.assertEquals("/trailing/slash+/" ,ContextUtil.joinContexts(true,true,"trailing/","slash+/"));

        Assert.assertEquals("leading/slash"     ,ContextUtil.joinContexts(false,false,"/leading","/slash"));
        Assert.assertEquals("/leading/slash+/"  ,ContextUtil.joinContexts(true,true,"/leading","/slash+"));

        Assert.assertEquals("double/slash"      ,ContextUtil.joinContexts(false,false,"/double/","/slash/"));
        Assert.assertEquals("/double/slash+/"   ,ContextUtil.joinContexts(true,true,"/double/","/slash+/"));

        Assert.assertEquals("no/slash"      ,ContextUtil.joinContexts(false,false,"no","slash"));
        Assert.assertEquals("/no/slash+/"   ,ContextUtil.joinContexts(true,true,"no","slash+"));

        Assert.assertEquals("cons/slash"    ,ContextUtil.joinContexts(false,false,"/cons/","/slash/"));
        Assert.assertEquals("/cons/slash+/" ,ContextUtil.joinContexts(true,true,"/cons/","/slash+/"));

        Assert.assertEquals("mix/slash"     ,ContextUtil.joinContexts(false,false,"\\mix\\","/slash/"));
        Assert.assertEquals("/mix/slash+/"  ,ContextUtil.joinContexts(true,true,"\\mix\\","/slash+/"));
    }

}
