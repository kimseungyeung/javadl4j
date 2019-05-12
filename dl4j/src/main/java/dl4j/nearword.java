package dl4j;

import java.io.File;
import java.util.Scanner;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

public class nearword {
public static Word2Vec vec;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File dir = new File("D:" + "/deeplearning/");
		if(!dir.exists()) {
			dir.mkdirs();
		}
		if(vec==null) {
		vec =WordVectorSerializer.readWord2VecModel("D:" + "/deeplearning/" + "pathToSaveModel.txt");
	}
		Scanner scan = new Scanner(System.in); // 문자 입력을 인자로 Scanner 생성

	System.out.println("검색값을 입력하세요:");

	String word = scan.nextLine();
	String result =vec.wordsNearest(word,10).toString();
	System.out.println(result);

	}

}
