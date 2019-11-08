package com.jstarcraft.nlp.solr;

import java.text.BreakIterator;
import java.util.Locale;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.core.utility.StringUtility;
import com.jstarcraft.nlp.lucene.NlpSegmenter;
import com.jstarcraft.nlp.tokenization.NlpToken;
import com.jstarcraft.nlp.tokenization.NlpTokenizer;

/**
 * Lucene分词工厂
 * 
 * @author Birdy
 *
 */
public abstract class NlpSegmentFactory extends TokenizerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(NlpSegmentFactory.class);

    /** 分句迭代器 */
    private BreakIterator iterator;

    /** 分词迭代器 */
    private NlpTokenizer<? extends NlpToken> tokenizer;

    protected BreakIterator getBreakIterator(Map<String, String> configurations) {
        // 统一装配分句迭代器
        BreakIterator iterator = null;
        String breakerType = get(configurations, "breakerType");
        if (StringUtility.isNotBlank(breakerType)) {
            String breakerLocale = get(configurations, "breakerLocale");
            Locale locale = null;
            if (StringUtility.isBlank(breakerType)) {
                locale = Locale.getDefault();
            } else {
                locale = new Locale(breakerLocale);
            }
            switch (breakerType) {
            case "Character":
                iterator = BreakIterator.getCharacterInstance(locale);
                break;
            case "Line":
                iterator = BreakIterator.getLineInstance(locale);
                break;
            case "Sentence":
                iterator = BreakIterator.getSentenceInstance(locale);
                break;
            case "Word":
                iterator = BreakIterator.getWordInstance(locale);
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        return iterator;
    }

    protected abstract NlpTokenizer<? extends NlpToken> getNlpIterator(Map<String, String> configurations);

    public NlpSegmentFactory(Map<String, String> configurations) {
        super(configurations);
        this.iterator = getBreakIterator(configurations);
        this.tokenizer = getNlpIterator(configurations);
    }

    @Override
    public Tokenizer create(AttributeFactory factory) {
        return new NlpSegmenter(factory, iterator, tokenizer);
    }

}