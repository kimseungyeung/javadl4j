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
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.KoreanTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;



public class learning {
	public static Word2Vec vec = null;
	public static int layersize = 100;
	public static int windowsize = 5; // ���� ������ ����
	public static int minword = 5; // �����ݺ��ܾ��
	public static int itter = 1; // ���� �ݺ���
	public static int seed = 0; // ��������
	public static int batchsize = 150; // �ѹ��� ó���ϴ� �����Ͱ�
	public static int epoch =5; // ��ü �ݺ���
	public static int worker = 1;// �����͸� �����ؼ� �н��ϹǷ� �н��ӵ��� ���������� �����͸� ����ó���ؼ� �н������ ����� �ȳ��ü�����
	public static double learningrate = 0.025;
	public static String filename = "test.txt";
	public static List<String> stopword;
	public static Collection<String> lst = null;
	public static SentenceIterator iter;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File dir = new File("D:" + "/deeplearning/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		Scanner scan = new Scanner(System.in); // ���� �Է��� ���ڷ� Scanner ����

		System.out.println("���ϸ��� �Է��ϼ���:");

		filename = scan.nextLine();

		learning();
	}

	public static void learning() {

		lst = null;

		File localFile = new File("D:" + "/deeplearning/", filename);

		// iter = new FileSentenceIterator(localFile);

		 try { 

		iter = new BasicLineIterator(localFile);
		// iter = new LineSentenceIterator(localFile);

		
		  } catch (FileNotFoundException e) { //
			/*
			 * Log.e("�̴�����",e.getMessage().toString()); System.out.println("�̴�����" +
			 * e.getMessage().toString());
			 */
		  }
		 
		// TokenizerFactory t = new DefaultTokenizerFactory();
		TokenizerFactory t = new ExcelTokenizerFactory(0);
		// TokenizerFactory t= new KoreanTokenizerFactory();
		t.setTokenPreProcessor(new TokenPreProcess() {

			@Override
			public String preProcess(String token) {
				// TODO Auto-generated method stub
				if (!token.equals(",")) {
					String s = token;
					s = s.replace("��", "");
					s = s.replace("��", "");
					s = s.replace(" ", "");
					s = s.replace("\"", "");
					String[] values = s.split(",");
					return values[0];
				} else {
					return null;
				}
			}
		});
		 t.setTokenPreProcessor(new CommonPreprocessor()); 

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
		stopword.add("��");
		stopword.add("��");
		stopword.add("��");
		stopword.add("��");
		stopword.add("��");
		stopword.add("��");
		stopword.add("��");

		System.out.println("�н�����");
		try {
			vec = new Word2Vec.Builder().minWordFrequency(minword) // ���� Ƚ���� minword ������ �ܾ�� ����
					.iterations(itter) // ����ݺ�Ƚ��
					.layerSize(layersize) // output layer size
					.windowSize(windowsize).iterate(iter)
//		                    .limitVocabularySize(1)  //����ϴ� ���ּ��� ����
//		                    .useHierarchicSoftmax(true)
//		                    .useAdaGrad(true)
//		                    .seed(seed) //�������� ����
					.tokenizerFactory(t).epochs(epoch) // ��ü�н��ݺ�
					.batchSize(batchsize)// ���� �����Ҷ� �ѹ��� ���� �ܾ� ��
					.stopWords(stopword) // �н��Ҷ� �����ϴ� �ܾ��� ����Ʈ
					.workers(worker)// �н��� ����ϴ� �������� ����
					.learningRate(learningrate) // �н������� 1���� �������۰Լ����ؾ���
					.build();

			vec.fit();
			
			System.out.println("�н���");
		} catch (Exception e) {
			System.out.println("����" + e.getMessage().toString());
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
			System.out.println("�Ϸ�:" + "�н��� ����Ǿ����ϴ�");
			Scanner scan2 = new Scanner(System.in);
			String result = scan2.nextLine();
			System.out.println(vec.wordsNearest(result, 10));
			String ss= "???";
		} catch (IllegalStateException e) {
			// ����
			System.out.println("����" + e.getMessage().toString());
		}
	}
}
