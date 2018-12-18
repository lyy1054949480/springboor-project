package com.baizhi.springboot;

import com.baizhi.springboot.entity.User;
import com.baizhi.springboot.mapper.UserMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Test
    public void contextLoads() throws Exception {
        List<Document> docs = new ArrayList<>();
        Document doc;
        List<User> list = userMapper.selectAll();
        for (User user : list) {
            doc = new Document();
            //如果是yes，说明存储到文档域
            Field username = new TextField("username",user.getUsername(), Field.Store.YES);
            Field userAge = new TextField("userAge",user.getUserAge().toString(),Field.Store.YES);

            doc.add(username);
            doc.add(userAge);
            docs.add(doc);
        }
        //创建分词器
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
        File indexFile = new File("F:\\luceneindex\\index1");
        //目录流对象
        Directory directory = FSDirectory.open(indexFile);
        IndexWriter writer = new IndexWriter(directory,config);

        for (Document document : docs) {
            writer.addDocument(document);
        }
        writer.close();
    }

    @Test
    public void indexSearch() throws  Exception{
        //username  默认搜索的filed域名
        //需要制定分词器，搜索时和索引时分词器要保持一致
        QueryParser parser = new QueryParser("username",new StandardAnalyzer());

        //通过parser创建Query对象  查询对象
        Query query = parser.parse("username:lyy");

        File indexFile = new File("F:\\luceneindex\\index1");
        Directory directory = FSDirectory.open(indexFile);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        //n  指定显示的条数
        TopDocs topDocs = searcher.search(query, 5);
        //根据查询条件匹配的记录条数
        int count = topDocs.totalHits;
        System.out.println("记录数：======"+count);
        //查询
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            //文档id
            int docId = scoreDoc.doc;
            Document document = searcher.doc(docId);
            System.out.println("------------"+document.get("username"));
            System.out.println("------------"+document.get("userAge"));

        }


    }

}

