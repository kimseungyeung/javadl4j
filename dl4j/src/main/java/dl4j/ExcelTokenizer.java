package dl4j;

import com.twitter.penguin.korean.KoreanTokenJava;
import com.twitter.penguin.korean.TwitterKoreanProcessorJava;
import scala.collection.Seq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;

/**
 * Created by kepricon on 16. 10. 20. KoreanTokenizer using KoreanTwitterText
 * (<a href=
 * "https://github.com/twitter/twitter-korean-text">https://github.com/twitter/twitter-korean-text</a>)
 */
public class ExcelTokenizer implements Tokenizer {
	private Iterator<String> tokenIter;
	private List<String> tokenList;

	private TokenPreProcess preProcess;

	public ExcelTokenizer(String toTokenize,int type) {

		// need normalize?
		
		// Tokenize
		String[] values = toTokenize.split(",");
		Seq<com.twitter.penguin.korean.tokenizer.KoreanTokenizer.KoreanToken> tokens = TwitterKoreanProcessorJava
				.tokenize(values[3]);
		tokenList = new ArrayList<>();
		Iterator<KoreanTokenJava> iter = TwitterKoreanProcessorJava.tokensToJavaKoreanTokenList(tokens).iterator();
		//문장단위로 끊어서 처리해야할경우 
		if(type==0) {
		while (iter.hasNext()) {
			String addd = iter.next().getText().trim();
			if (!addd.equals(",") && !addd.equals("") && !addd.equals("\"") && !addd.equals("[") && !addd.equals("]")) {
				tokenList.add(addd);
			}
		}
		}
		//단어로 표시해야할경우 (띄어쓰기 그대로 표시하기위함)
		else if(type==1) {
		tokenList.add(values[3]);
		}
		tokenIter = tokenList.iterator();
	}

	@Override
	public boolean hasMoreTokens() {
		return tokenIter.hasNext();
	}

	@Override
	public int countTokens() {
		return tokenList.size();
	}

	@Override
	public String nextToken() {
		if (hasMoreTokens() == false) {
			throw new NoSuchElementException();
		}
		return this.preProcess != null ? this.preProcess.preProcess(tokenIter.next()) : tokenIter.next();
	}

	@Override
	public List<String> getTokens() {
		return tokenList;
	}

	@Override
	public void setTokenPreProcessor(TokenPreProcess tokenPreProcess) {
		this.preProcess = tokenPreProcess;
	}
}
