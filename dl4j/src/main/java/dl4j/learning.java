package dl4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.FileSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

public class learning {
	public static Word2Vec vec = null;
	public static int layersize = 100;
	public static int windowsize = 5; // 문장 나누는 구분
	public static int minword = 5; // 최저 반복단어수
	public static int itter = 1; // 문장 반복수
	public static int seed = 0; // 랜덤난수
	public static int batchsize = 512; // 한번에 처리하는 데이터값
	public static int epoch = 5; // 전체 반복수
	public static int worker = 1;// 데이터를 분할해서 학습하므로 학습속도가 빨라지지만 데이터를 분할처리해서 학습결과가 제대로 안나올수있음
	public static double learningrate = 0.025;
	public static String filename = "test.txt";
	public static List<String> stopword;
	public static Collection<String> lst = null;
	public static SentenceIterator iter;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scan = new Scanner(System.in); // 문자 입력을 인자로 Scanner 생성

		System.out.println("파일명을 입력하세요:");

		filename = scan.nextLine();

		learning();
	}

	public static void learning() {
		File dir = new File("D:" + "/deeplearning/");
		if(!dir.exists()) {
			dir.mkdirs();
		}
		lst = null;

		File localFile = new File("D:" + "/deeplearning/", filename);

		    //    iter = new FileSentenceIterator(localFile);

		/* try { */

			//iter = new BasicLineIterator(localFile);
			iter = new LineSentenceIterator(localFile);
	/*	} catch (FileNotFoundException e) {
//		            Log.e("이더실패",e.getMessage().toString());
			System.out.println("이더실패" + e.getMessage().toString());
		}*/
		TokenizerFactory t = new DefaultTokenizerFactory();
		t.setTokenPreProcessor(new CommonPreprocessor());
		iter.setPreProcessor(new SentencePreProcessor() {

			@Override
			public String preProcess(String sentence) {
				// TODO Auto-generated method stub
				String s = sentence;
				s = s.replace("“", "");
				s = s.replace("”", "");
				return s;
			}
		});
//		        log.info("Building model....");
		stopword = new ArrayList<String>();
		stopword.add(" ");
		stopword.add(".");
		stopword.add(",");
		stopword.add("?");
		stopword.add("\"");
		stopword.add("?");
		stopword.add(">");
		stopword.add("<");
		stopword.add("\\");
		stopword.add("%");
		stopword.add("＂");
		stopword.add("”");
		stopword.add("˝");
		stopword.add("″");
		stopword.add("”");
		stopword.add("“");
		stopword.add("”");

		System.out.println("학습시작");
		try {
			vec = new Word2Vec.Builder()
					.minWordFrequency(minword) // 등장 횟수가 minword 이하인 단어는 무시
					.iterations(itter) // 문장반복횟수
					.layerSize(layersize) // output layer size
					.windowSize(windowsize)
					.iterate(iter)
//		                    .limitVocabularySize(1)  //사용하는 어휘수의 제한
//		                    .useHierarchicSoftmax(true)
//		                    .useAdaGrad(true)
//		                    .seed(seed) //랜덤난수 적용
					.tokenizerFactory(t)
					.epochs(epoch) // 전체학습반복
					.batchSize(batchsize)// 사전 구축할때 한번에 읽을 단어 수
					.stopWords(stopword) // 학습할때 무시하는 단어의 리스트
					.workers(worker)// 학습시 사용하는 쓰레드의 갯수
					.learningRate(learningrate) // 학습설정시 1보다 무조건작게설정해야함
					.build();

			vec.fit();
				
			System.out.println("학습중");
		} catch (Exception e) {
			System.out.println("에러" + e.getMessage().toString());
		}
		final Collection<String> list = vec.getStopWords();

		File f = new File("D:" + "/deeplearning/" + "pathToSaveModel.txt");
		if (f != null) {
			if (f.exists()) {
				f.delete();
			}
		}
		try {
//		            WordVectorSerializer.writeFullModel(vec, Environment.getExternalStorageDirectory().getAbsolutePath() + "/deeplearning/" + "pathToSaveModel.txt");
			WordVectorSerializer.writeWord2VecModel(vec, "D:" + "/deeplearning/" + "pathToSaveModel.txt");
			// WordVectorSerializer.writeWord2VecModel(vec,f);
			System.out.println("완료:" + "학습이 종료되었습니다");
			Scanner scan2 = new Scanner(System.in);
			String result = scan2.nextLine();
			System.out.println(vec.wordsNearest(result,10));
		} catch (IllegalStateException e) {
			// 에러
			System.out.println("에러" + e.getMessage().toString());
		}
	}
}
