package com.gilead.payments;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Flat JSON for the four payment fields and the receipt. Enough for this demo;
 * a production API would use its usual JSON library.
 */
final class PaymentJson {

    private PaymentJson() {
    }

    static PaymentRequest request(String json) {
        Map<String, String> fields = object(json);
        String amount = fields.get("amountMinor");
        if (amount == null) {
            throw new IllegalArgumentException("amountMinor is required");
        }
        long amountMinor;
        try {
            amountMinor = Long.parseLong(amount);
        } catch (NumberFormatException failure) {
            throw new IllegalArgumentException("amountMinor must be an integer");
        }
        return new PaymentRequest(
                fields.get("customerId"),
                fields.get("billReference"),
                amountMinor,
                fields.get("currency"));
    }

    static String receipt(PaymentReceipt receipt) {
        return "{"
                + "\"paymentId\":" + quote(receipt.paymentId())
                + ",\"outcome\":" + quote(receipt.outcome().name())
                + ",\"customerId\":" + quote(receipt.customerId())
                + ",\"billReference\":" + quote(receipt.billReference())
                + ",\"amountMinor\":" + receipt.amountMinor()
                + ",\"currency\":" + quote(receipt.currency())
                + ",\"detail\":" + quote(receipt.detail())
                + "}";
    }

    static String error(String message) {
        return "{\"error\":" + quote(message) + "}";
    }

    private static Map<String, String> object(String json) {
        if (json == null) {
            throw new IllegalArgumentException("JSON body is required");
        }
        String body = json.trim();
        if (body.length() < 2 || body.charAt(0) != '{' || body.charAt(body.length() - 1) != '}') {
            throw new IllegalArgumentException("JSON object is required");
        }
        Map<String, String> fields = new LinkedHashMap<>();
        int i = 1;
        while (i < body.length() - 1) {
            i = skip(body, i);
            if (i >= body.length() - 1) {
                break;
            }
            if (body.charAt(i) != '"') {
                throw new IllegalArgumentException("JSON field name is required");
            }
            Token name = string(body, i);
            i = skip(body, name.next());
            if (i >= body.length() || body.charAt(i) != ':') {
                throw new IllegalArgumentException("JSON field is missing a colon");
            }
            i = skip(body, i + 1);
            String value;
            if (i < body.length() && body.charAt(i) == '"') {
                Token token = string(body, i);
                value = token.value();
                i = token.next();
            } else {
                int start = i;
                while (i < body.length() && body.charAt(i) != ',' && body.charAt(i) != '}') {
                    i++;
                }
                value = body.substring(start, i).trim();
            }
            fields.put(name.value(), value);
            i = skip(body, i);
            if (i < body.length() && body.charAt(i) == ',') {
                i++;
            }
        }
        return fields;
    }

    private static Token string(String body, int openingQuote) {
        StringBuilder value = new StringBuilder();
        for (int i = openingQuote + 1; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '\\') {
                if (i + 1 >= body.length()) {
                    throw new IllegalArgumentException("JSON string is not closed");
                }
                char escaped = body.charAt(++i);
                value.append(switch (escaped) {
                    case '"', '\\', '/' -> escaped;
                    case 'n' -> '\n';
                    case 't' -> '\t';
                    default -> throw new IllegalArgumentException("Unsupported JSON escape");
                });
                continue;
            }
            if (c == '"') {
                return new Token(value.toString(), i + 1);
            }
            value.append(c);
        }
        throw new IllegalArgumentException("JSON string is not closed");
    }

    private record Token(String value, int next) {
    }

    private static int skip(String body, int i) {
        while (i < body.length() && Character.isWhitespace(body.charAt(i))) {
            i++;
        }
        return i;
    }

    private static String quote(String value) {
        return "\"" + value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                + "\"";
    }
}
