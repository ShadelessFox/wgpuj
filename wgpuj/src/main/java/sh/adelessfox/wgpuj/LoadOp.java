package sh.adelessfox.wgpuj;

@SuppressWarnings("unused")
public sealed interface LoadOp<T> {
    record Clear<T>(T value) implements LoadOp<T> {
    }

    record Load<T>() implements LoadOp<T> {
    }
}
