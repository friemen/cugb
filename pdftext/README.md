# pdftext

Demo of PDF text extraction with Clojure (based on [PDFBox](https://pdfbox.apache.org/)).


**Problem**: PDFBox provides a
[PDFTextStripper](https://pdfbox.apache.org/apidocs/org/apache/pdfbox/util/PDFTextStripper.html)
class that requires us to use a base class to override the
`processTextPosition()` method. This is essentially a *push* or
*callback-based* API, because PDFTextStripper provides one character
after the other to our overridden method. Thus we're no more in control!

Instead we'd prefer a sequence that we can use to *pull*
characters from a PDF. This would leave full control in our code.


We solve this problem 4 times:


1. Use an atom within the processTextPosition implementation to store
all text positions. **BUT** this doesn't work well with large PDFs.

2. Use a [core.async](https://clojure.github.io/core.async/) channel and [`lazy-seq`](http://conj.io/store/v1/org.clojure/clojure/latest/clj/clojure.core/lazy-seq/). The `processTextPosition` impl pushes text positions from a different thread to the channel and a lazy sequence (implemented on the basis of `lazy-seq`). **BUT** if we don't consume all available text positions we will likely create numerous blocked threads that are waiting to write the next text position into the channel (that no one is reading anymore).

3. Implement the lazy sequence as a new type and use a reference counter which is decremented with a finalizer to make sure the write task in the thread is properly stopped. **BUT** this implementation is far from simple, and blocked threads are only free'd when GC kicks in.

4. Implement a closeable lazy sequence and treat it like a resource to
   be used with
   [`with-open`](http://conj.io/store/v1/org.clojure/clojure/latest/clj/clojure.core/with-open/). This changes the API a bit but is much simpler.
