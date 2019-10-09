/*
 * Created by Michael Carrara <michael.carrara@breadwallet.com> on 7/1/19.
 * Copyright (c) 2019 Breadwinner AG.  All right reserved.
*
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package com.breadwallet.corenative.crypto;

import com.breadwallet.corenative.CryptoLibrary;
import com.google.common.base.Optional;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class BRCryptoTransfer extends PointerType {

    public static BRCryptoTransfer createOwned(BRCryptoTransfer transfer) {
        // TODO(fix): Can the use case here (called when parsed out of struct) be replaced by changing struct to
        //            have BRCryptoTransfer.OwnedBRCryptoTransfer as its field, instead of BRCryptoTransfer?
        return new OwnedBRCryptoTransfer(transfer.getPointer());
    }

    public BRCryptoTransfer(Pointer address) {
        super(address);
    }

    public BRCryptoTransfer() {
        super();
    }

    public Optional<BRCryptoAddress> getSourceAddress() {
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoTransferGetSourceAddress(this));
    }

    public Optional<BRCryptoAddress> getTargetAddress() {
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoTransferGetTargetAddress(this));
    }

    public BRCryptoAmount getAmount() {
        return CryptoLibrary.INSTANCE.cryptoTransferGetAmount(this);
    }

    public BRCryptoAmount getAmountDirected() {
        return CryptoLibrary.INSTANCE.cryptoTransferGetAmountDirected(this);
    }

    public Optional<BRCryptoHash> getHash() {
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoTransferGetHash(this));
    }

    public BRCryptoTransferDirection getDirection() {
        return BRCryptoTransferDirection.fromNative(CryptoLibrary.INSTANCE.cryptoTransferGetDirection(this));
    }

    public BRCryptoTransferState getState() {
        return CryptoLibrary.INSTANCE.cryptoTransferGetState(this);
    }

    public Optional<BRCryptoFeeBasis> getEstimatedFeeBasis() {
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoTransferGetEstimatedFeeBasis(this));
    }

    public Optional<BRCryptoFeeBasis> getConfirmedFeeBasis() {
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoTransferGetConfirmedFeeBasis(this));
    }

    public BRCryptoUnit getUnitForFee() {
        return CryptoLibrary.INSTANCE.cryptoTransferGetUnitForFee(this);
    }

    public BRCryptoUnit getUnitForAmount() {
        return CryptoLibrary.INSTANCE.cryptoTransferGetUnitForAmount(this);
    }

    public boolean isIdentical(BRCryptoTransfer other) {
        return BRCryptoBoolean.CRYPTO_TRUE == CryptoLibrary.INSTANCE.cryptoTransferEqual(this, other);
    }

    public static class OwnedBRCryptoTransfer extends BRCryptoTransfer {

        public OwnedBRCryptoTransfer(Pointer address) {
            super(address);
        }

        public OwnedBRCryptoTransfer() {
            super();
        }

        @Override
        protected void finalize() {
            if (null != getPointer()) {
                CryptoLibrary.INSTANCE.cryptoTransferGive(this);
            }
        }
    }
}
