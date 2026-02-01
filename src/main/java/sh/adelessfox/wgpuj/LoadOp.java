package sh.adelessfox.wgpuj;

public sealed interface LoadOp<T> {
    record Clear<T>(T value) implements LoadOp<T> {
    }

    record Load<T>() implements LoadOp<T> {
    }

    record DontCare<T>() implements LoadOp<T> {
    }
}
