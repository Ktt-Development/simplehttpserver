package com.kttdevelopment.simplehttpserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public final class ContextUtilTests {

    private static final class test{

        private final String expected, context;
        private final boolean leadingSlash, trailingSlash;

        public test(final String expected, final String context, final boolean leadingSlash, final boolean trailingSlash){
            this.expected       = expected;
            this.context        = context;
            this.leadingSlash   = leadingSlash;
            this.trailingSlash  = trailingSlash;
        }

    }

    @Test
    public final void testContexts(){
        final test[] tests = {
            new test(""                                 , "/"                               , false  , false),
            new test("/"                                , ""                                , true   , false),
            new test("/"                                , ""                                , false  , true ),
            new test("/"                                , ""                                , true   , true ),
            new test("a"                                , "a"                               , false  , false),
            new test("/a"                               , "a"                               , true   , false),
            new test("a/"                               , "a"                               , false  , true ),
            new test("/a/"                              , "a"                               , true   , true ),
            new test("testNone"                         , "/testNone/"                      , false  , false),
            new test("/testLeading"                     , "testLeading"                     , true   , false),
            new test("testTrailing/"                    , "testTrailing"                    , false  , true ),
            new test("/testBoth/"                       , "testBoth"                        , true   , true ),
            new test("testNoneBackSlash"                , "\\testNoneBackSlash\\"           , false  , false),
            new test("/testBackSlash/"                  , "\\testBackSlash\\"               , true   , true ),
            new test("/testConsecutiveBackSlash/"       , "\\\\testConsecutiveBackSlash\\\\", true   , true ),
            new test("/testConsecutiveForwardSlash/"    , "//testConsecutiveForwardSlash//" , true   , true ),
            new test("/testWhitespace/"                 , " /testWhitespace/ "              , true   , true),
            new test("/ testWhitespace /"               , "/ testWhitespace /"              , true   , true),
            new test(" testWhitespace "                 , "/ testWhitespace /"              , false  , false),
            new test("testWhitespace"                   , " testWhitespace "                , false  , false),
            new test("/testWhitespace/"                 , " /testWhitespace/ "              , true   , true),
        };

        for(final test test : tests)
            Assertions.assertEquals(test.expected, ContextUtil.getContext(test.context, test.leadingSlash, test.trailingSlash), String.format("Incorrect context for #(\"%s\", %s, %s)", test.context, test.leadingSlash, test.trailingSlash));
    }

    //

    private static final class testJoin {

        private final String expected;
        private final String[] contexts;
        private final boolean leadingSlash, trailingSlash;

        public testJoin(final String expected, final boolean leadingSlash, final boolean trailingSlash, final String... contexts){
            this.expected       = expected;
            this.leadingSlash   = leadingSlash;
            this.trailingSlash  = trailingSlash;
            this.contexts       = contexts;
        }

    }

    @Test
    public final void testJoin(){
        final testJoin[] tests = {
            new testJoin("testBlank"            , false  , false  ,"testBlank",""),
            new testJoin("/testBlank/"          , true   , true   ,"testBlank",""),
            new testJoin("testBlank"            , false  , false  ,"","testBlank"),
            new testJoin("/testBlank/"          , true   , true   ,"","testBlank"),
            new testJoin(""                     , false  , false  ,"",""),
            new testJoin("/"                    , true   , true   ,"",""),
            new testJoin("trailing/slash"       , false , false ,"trailing/","slash/"),
            new testJoin("/trailing/slash/"     , true  , true  ,"trailing/","slash/"),
            new testJoin("leading/slash"        , false , false ,"leading/","slash/"),
            new testJoin("/leading/slash/"      , true  , true  ,"leading/","slash/"),
            new testJoin("double/slash"         , false , false ,"/double/","/slash/"),
            new testJoin("/double/slash/"       , true  , true  ,"/double/","/slash/"),
            new testJoin("no/slash"             , false , false ,"no","slash"),
            new testJoin("/no/slash/"           , true  , true  ,"no","slash"),
            new testJoin("consecutive/slash"    , false , false ,"//consecutive//","//slash//"),
            new testJoin("/consecutive/slash/"  , true  , true  ,"//consecutive//","//slash//"),
            new testJoin("mixed/slash"          , false , false ,"\\mixed\\","//slash//"),
            new testJoin("/mixed/slash/"        , true  , true  ,"\\mixed\\","//slash//"),
        };

        for(final testJoin test : tests)
            Assertions.assertEquals(test.expected, ContextUtil.joinContexts(test.leadingSlash, test.trailingSlash, test.contexts), String.format("Incorrect context for #(%s, %s, %s)", test.leadingSlash, test.trailingSlash, Arrays.toString(test.contexts)));
    }

}
