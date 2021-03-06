/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package asset.pipeline

import grails.util.Holders
import spock.lang.Specification

/**
 * @author David Estes
 */
class AssetHelperSpec extends Specification {

    // void "should get byte digest based on file contents and config settings"() {
    //     given:
    //         Holders.metaClass.static.getConfig = { ->
    //             [grails: [assets: [[minifyJs: true]]]]
    //         }
    //         def sampleText = "Sample Text"
    //         def md5Sum
    //     when:
    //         md5Sum = AssetHelper.getByteDigest(sampleText.bytes)
    //     then:
    //         md5Sum == '5a15e5d8bb4e8f08c4d76e72894491c6'
    //     when:
    //         Holders.metaClass.static.getConfig = { ->
    //             [grails: [assets: [[minifyJs: false]]]]
    //         }
    //         md5Sum = AssetHelper.getByteDigest(sampleText.bytes)
    //     then:
    //         md5Sum == '40901b0561d538750b3da0a7371fb6a2'
    // }


    void "should get file specs by contentType"() {
    	when:	
    		def assetHelper = AssetHelper
    	then:
	    	fileSpecs as Set == assetHelper.getPossibleFileSpecs(contentType) as Set
	    where:
	    	contentType                | fileSpecs
	    	'application/javascript'   | [JsAssetFile]
	    	'application/x-javascript' | [JsAssetFile]
	    	'text/css'                 | [CssAssetFile]
	    	'blob/text'                | []

    }

    void "should get asset file object based on file name and extension" () {
    	given: 
            def testFileName = 'asset-pipeline/test/test'
            def testFileExt = "css"
            def assetFile
        when: 
        	assetFile = AssetHelper.getAssetFileWithExtension(testFileName, testFileExt)
        then:
        	assetFile?.name == 'test.css'
        
        when: 
	        testFileName = 'asset-pipeline/test/test'
            testFileExt = "sass"
        	assetFile = AssetHelper.getAssetFileWithExtension(testFileName, testFileExt)
        then:
        	assetFile == null

        when: 
	        testFileName = 'asset-pipeline/test/test.css'
	        testFileExt = null
        	assetFile = AssetHelper.getAssetFileWithExtension(testFileName, testFileExt)
        then:
        	assetFile?.name == 'test.css'
    }

    void "should copy file"() {
    	given: 
	        def testFileName = 'grails-app/assets/stylesheets/asset-pipeline/test/test-copy.css'
	        def destFileName = 'grails-app/assets/stylesheets/asset-pipeline/test/test-copy2.css'
	        def testFile = new File(testFileName)
	        testFile.text = "Testing123"
	        def destFile = new File(destFileName)
	    when: 
	    	AssetHelper.copyFile(testFile, destFile)
	    then:
	    	destFile.text == testFile.text
	   	cleanup:
		   	testFile.delete()
	   		destFile.delete()
    }

    void "should provide file name without the extension"() {
    	given: 
	    	def testName = "test.min.js"
	    	def nameWithoutExt
    	when:
    		nameWithoutExt = AssetHelper.nameWithoutExtension(testName)
    	then:
    		nameWithoutExt == 'test.min'
    }

    void "should extract extension from file name"() {
    	given: 
	    	def testName = "test.min.js"
	    	def ext
    	when:
    		ext = AssetHelper.extensionFromURI(testName)
    	then:
    		ext == 'js'
    }
}
