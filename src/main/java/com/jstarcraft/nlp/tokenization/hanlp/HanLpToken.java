package com.jstarcraft.nlp.tokenization.hanlp;

import java.util.Iterator;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;
import com.jstarcraft.nlp.analysis.lexical.tag.chinses.PekingUniversity;
import com.jstarcraft.nlp.tokenization.NlpTag;
import com.jstarcraft.nlp.tokenization.NlpToken;

/**
 * HanLP词元
 * 
 * @author Birdy
 *
 */
public class HanLpToken implements Iterable<HanLpToken>, Iterator<HanLpToken>, NlpToken {

    private Iterator<Term> iterator;

    private Term term;

    public HanLpToken(Iterator<Term> iterator) {
        this.iterator = iterator;
    }

    @Override
    public Iterator<HanLpToken> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public HanLpToken next() {
        term = iterator.next();
        return this;
    }

    @Override
    public String getTerm() {
        return term.word;
    }

    @Override
    public int getBegin() {
        return term.offset;
    }

    @Override
    public int getEnd() {
        return getBegin() + getTerm().length();
    }

    @Override
    public NlpTag getTag() {
        Nature nature = term.nature;
        if (nature == Nature.begin) {
            return NlpTag.X;
        }
        if (nature == Nature.end) {
            return NlpTag.X;
        }
        /** 学术词 */
        if (nature.firstChar() == 'g') {
            return NlpTag.N;
        }
        return PekingUniversity.getTag(nature.toString());
    }

    @Override
    public String getNature() {
        Nature nature = term.nature;
        return nature.toString();
    }

}
